package com.msquare.widget.mprogressbar;

/**
 * Created by Moinkhan on 20-01-2016.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;import com.msquare.widget.mprogressbar.R;import java.lang.IllegalArgumentException;import java.lang.Math;import java.lang.Override;

public class MProgressBar extends View {

    private Paint mPrimaryPaint;
    private Paint mSecondaryPaint;
    private RectF mRectF;
    private TextPaint mTextPaint;
    private Paint mBackgroundPaint;

    private boolean mDrawText = false;

    private int mSecondaryProgressColor;
    private int mPrimaryProgressColor;
    private int mBackgroundColor;

    private int mStrokeWidth;

    private int mProgress;
    private int mSecodaryProgress;

    private int mTextColor;

    private int mPrimaryCapSize;
    private int mSecondaryCapSize;
    private boolean mIsPrimaryCapVisible;
    private boolean mIsSecondaryCapVisible;

    private int x;
    private int y;
    private int mWidth = 0, mHeight = 0;


    public MProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public MProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        TypedArray a;
        if (attrs != null) {
            a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.MProgressBar,
                    0, 0);
        } else {
            throw new IllegalArgumentException("Must have to pass the attributes");
        }

        try {
            mDrawText = a.getBoolean(R.styleable.MProgressBar_showProgressText, false);

            mBackgroundColor = a.getColor(R.styleable.MProgressBar_backgroundColor, android.R.color.darker_gray);
            mPrimaryProgressColor = a.getColor(R.styleable.MProgressBar_progressColor, android.R.color.darker_gray);
            mSecondaryProgressColor = a.getColor(R.styleable.MProgressBar_secondaryProgressColor, android.R.color.black);

            mProgress = a.getInt(R.styleable.MProgressBar_progress, 0);
            mSecodaryProgress = a.getInt(R.styleable.MProgressBar_secondaryProgress, 0);

            mStrokeWidth = a.getDimensionPixelSize(R.styleable.MProgressBar_strokeWidth, 20);
            mTextColor = a.getColor(R.styleable.MProgressBar_textColor, android.R.color.black);

            mPrimaryCapSize = a.getInt(R.styleable.MProgressBar_primaryCapSize, 20);
            mSecondaryCapSize = a.getInt(R.styleable.MProgressBar_secodaryCapSize, 20);

            mIsPrimaryCapVisible = a.getBoolean(R.styleable.MProgressBar_primaryCapVisibility, true);
            mIsSecondaryCapVisible = a.getBoolean(R.styleable.MProgressBar_secodaryCapVisibility, true);
        } finally {
            a.recycle();
        }

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(mStrokeWidth);
        mBackgroundPaint.setColor(mBackgroundColor);

        mPrimaryPaint = new Paint();
        mPrimaryPaint.setAntiAlias(true);
        mPrimaryPaint.setStyle(Paint.Style.STROKE);
        mPrimaryPaint.setStrokeWidth(mStrokeWidth);
        mPrimaryPaint.setColor(mPrimaryProgressColor);

        mSecondaryPaint = new Paint();
        mSecondaryPaint.setAntiAlias(true);
        mSecondaryPaint.setStyle(Paint.Style.STROKE);
        mSecondaryPaint.setStrokeWidth(mStrokeWidth - 2);
        mSecondaryPaint.setColor(mSecondaryProgressColor);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);

        mRectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
        mTextPaint.setTextSize(w / 5);
        x = (w / 2) - ((int) (mTextPaint.measureText(mProgress + "%") / 2));
        y = (int) ((h / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
        mWidth = w;
        mHeight = h;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPrimaryPaint.setStyle(Paint.Style.STROKE);
        mSecondaryPaint.setStyle(Paint.Style.STROKE);

        // for drawing a full progress .. The background circle
        canvas.drawArc(mRectF, 0, 360, false, mBackgroundPaint);

        // for drawing a secondary progress circle
        int secondarySwipeangle = (mSecodaryProgress * 360) / 100;
        canvas.drawArc(mRectF, 270, secondarySwipeangle, false, mSecondaryPaint);

        // for drawing a main progress circle
        int primarySwipeangle = (mProgress * 360) / 100;
        canvas.drawArc(mRectF, 270, primarySwipeangle, false, mPrimaryPaint);

        // for cap of secondary progress
        int r = (getHeight() - getPaddingLeft() * 2) / 2;      // Calculated from canvas width
        double trad = (secondarySwipeangle - 90) * (Math.PI / 180d); // = 5.1051
        int x = (int) (r * Math.cos(trad));
        int y = (int) (r * Math.sin(trad));
        mSecondaryPaint.setStyle(Paint.Style.FILL);
        if (mIsSecondaryCapVisible)
            canvas.drawCircle(x + (mWidth / 2), y + (mHeight / 2), mSecondaryCapSize, mSecondaryPaint);

        // for cap of primary progress
        trad = (primarySwipeangle - 90) * (Math.PI / 180d); // = 5.1051
        x = (int) (r * Math.cos(trad));
        y = (int) (r * Math.sin(trad));
        mPrimaryPaint.setStyle(Paint.Style.FILL);
        if (mIsPrimaryCapVisible)
            canvas.drawCircle(x + (mWidth / 2), y + (mHeight / 2), mPrimaryCapSize, mPrimaryPaint);


        if (mDrawText)
            canvas.drawText(mProgress + "%", x, y, mTextPaint);
    }

    public void setDrawText(boolean mDrawText) {
        this.mDrawText = mDrawText;
        invalidate();
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        invalidate();
    }

    public void setSecondaryProgressColor(int mSecondaryProgressColor) {
        this.mSecondaryProgressColor = mSecondaryProgressColor;
        invalidate();
    }

    public void setPrimaryProgressColor(int mPrimaryProgressColor) {
        this.mPrimaryProgressColor = mPrimaryProgressColor;
        invalidate();
    }

    public void setStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        invalidate();
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    public void setSecondaryProgress(int mSecondaryProgress) {
        this.mSecodaryProgress = mSecondaryProgress;
        invalidate();
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        invalidate();
    }

    public void setPrimaryCapSize(int mPrimaryCapSize) {
        this.mPrimaryCapSize = mPrimaryCapSize;
        invalidate();
    }

    public void setSecondaryCapSize(int mSecondaryCapSize) {
        this.mSecondaryCapSize = mSecondaryCapSize;
        invalidate();
    }

    public boolean isPrimaryCapVisible() {
        return mIsPrimaryCapVisible;
    }

    public void setIsPrimaryCapVisible(boolean mIsPrimaryCapVisible) {
        this.mIsPrimaryCapVisible = mIsPrimaryCapVisible;
    }

    public boolean isSecondaryCapVisible() {
        return mIsSecondaryCapVisible;
    }

    public void setIsSecondaryCapVisible(boolean mIsSecondaryCapVisible) {
        this.mIsSecondaryCapVisible = mIsSecondaryCapVisible;
    }


    public int getSecondaryProgressColor() {
        return mSecondaryProgressColor;
    }

    public int getPrimaryProgressColor() {
        return mPrimaryProgressColor;
    }

    public int getProgress() {
        return mProgress;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getSecodaryProgress() {
        return mSecodaryProgress;
    }

    public int getPrimaryCapSize() {
        return mPrimaryCapSize;
    }

    public int getSecondaryCapSize() {
        return mSecondaryCapSize;
    }
}
