package com.tokopedia.imagepicker.editor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.imagepicker.R;

import java.util.Map;
import java.util.TreeMap;

public class TwoLineSeekBar extends View {


    private static final float DEF_THUMB_RADIUS = 7.9875f;
    private static final float DEF_NAIL_RADIUS = 3.99375f;
    private static final float DEF_NAIL_STROKE_WIDTH = 7.9875f;
    private static final float DEF_LINE_WIDTH = 5.3250003f;
    public static final int SHADOW_RADIUS = 6;

    private float mDefaultAreaRadius = 0.0f;
    private OnSeekDefaultListener mDefaultListener;
    private OnSeekDownListener mDownListener;
    private GestureDetector mGestureDetector;
    private SeekBarGestureListener mGestureListener;
    private Paint mHighLightLinePaint;
    private Paint mLinePaint1;
    private Paint mLinePaint2;
    private OnSeekChangeListener mListener;
    private float mNailOffset;
    private Paint mNailPaint;
    private Map<String, Integer> mSavedColors;
    private Scroller mScroller;
    private float mSeekLength;
    private float mSeekLineEnd;
    private float mSeekLineStart;
    private int mStartValue;
    private float mStep;
    private float mThumbOffset;
    private Paint mThumbPaint;

    private float mThumbRadius = DEF_THUMB_RADIUS;
    private float mNailRadius = DEF_NAIL_RADIUS;
    private float mNailStrokeWidth = DEF_NAIL_STROKE_WIDTH;
    private float mHighStrokeWidth = DEF_NAIL_STROKE_WIDTH;
    private float mLineWidth = DEF_LINE_WIDTH;

    private int mNailColor;
    private int mThumbColor;
    private int mLineColor;
    private int mHighColor;

    private int mMaxValue = 0x64;
    private int mCurrentValue = 0x32;
    private int mDefaultValue = 0x32;
    private boolean mEnableTouch = true;
    private Rect mCircleRect = new Rect();
    private boolean mIsGlobalDrag = true;
    private boolean mIsTouchCircle = false;
    private boolean mSupportSingleTap = true;
    private Path path;

    public TwoLineSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TwoLineSeekBar, 0, 0);

        mThumbRadius = a.getDimension(R.styleable.TwoLineSeekBar_thumb_radius, DEF_THUMB_RADIUS);
        mNailRadius = a.getDimension(R.styleable.TwoLineSeekBar_nail_radius, DEF_NAIL_RADIUS);
        mNailStrokeWidth = a.getDimension(R.styleable.TwoLineSeekBar_nail_stroke_width, DEF_NAIL_STROKE_WIDTH);
        mHighStrokeWidth = a.getDimension(R.styleable.TwoLineSeekBar_high_stroke_width, DEF_NAIL_STROKE_WIDTH);
        mLineWidth = a.getDimension(R.styleable.TwoLineSeekBar_line_width, DEF_LINE_WIDTH);

        mNailColor = a.getColor(R.styleable.TwoLineSeekBar_nail_color, 0xFFFFE325);
        mThumbColor = a.getColor(R.styleable.TwoLineSeekBar_thumb_color, 0xFFFFE325);
        mLineColor = a.getColor(R.styleable.TwoLineSeekBar_line_color, 0xFFFFFFFF);
        mHighColor = a.getColor(R.styleable.TwoLineSeekBar_high_color, 0xFFFFE325);

        mDefaultAreaRadius = ((((mThumbRadius - mNailRadius) - mNailStrokeWidth) + mThumbRadius) / 2.0f);

        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mGestureListener = new SeekBarGestureListener();
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mNailPaint = new Paint();
        mNailPaint.setAntiAlias(true);
        mNailPaint.setColor(mNailColor);
        mNailPaint.setStrokeWidth(mNailStrokeWidth);
        mNailPaint.setStyle(Paint.Style.STROKE);
        mThumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(mThumbColor);
        mThumbPaint.setStyle(Paint.Style.FILL);
        mThumbPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, androidx.core.content.ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN500));

        // Important for certain APIs
        setLayerType(LAYER_TYPE_SOFTWARE, mThumbPaint);

        mLinePaint1 = new Paint();
        mLinePaint1.setAntiAlias(true);
        mLinePaint1.setColor(mLineColor);
        mLinePaint1.setAlpha(0xc8);
        mLinePaint2 = new Paint();
        mLinePaint2.setAntiAlias(true);
        mLinePaint2.setColor(mLineColor);
        mLinePaint2.setAlpha(0xc8);
        mHighLightLinePaint = new Paint();
        mHighLightLinePaint.setAntiAlias(true);
        mHighLightLinePaint.setColor(mHighColor);
        mHighLightLinePaint.setStrokeWidth(mHighStrokeWidth);
        mHighLightLinePaint.setStrokeCap(Paint.Cap.ROUND);
//        mHighLightLinePaint.setAlpha(0xc8);
        mSupportSingleTap = true;

        path = new Path();
    }

    public float dpToPixel(float dp) {
        return (getResources().getDisplayMetrics().density * dp);
    }

    public void setSingleTapSupport(boolean support) {
        mSupportSingleTap = support;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        if (hmode == -0x8000) {
            int hsize = Math.round((mThumbRadius * 2.0f));
            hsize += (getPaddingTop() + getPaddingBottom());
            int wsize = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(wsize, hsize);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        if (mSeekLength == 0) {
            int width = getWidth();
            mSeekLength = ((float) ((width - getPaddingLeft()) - getPaddingRight()) - (mThumbRadius * 2.0f));
            mSeekLineStart = ((float) getPaddingLeft() + mThumbRadius);
            mSeekLineEnd = ((float) (width - getPaddingRight()) - mThumbRadius);
            int currValue = Math.max(0x0, mCurrentValue);
            mNailOffset = ((mSeekLength * (float) mDefaultValue) / (float) mMaxValue);
            if ((mDefaultValue == 0) || (mDefaultValue == mMaxValue)) {
                mThumbOffset = ((mSeekLength * (float) currValue) / (float) mMaxValue);
            } else {
                float defaultAreaLength = mDefaultAreaRadius * 2.0f;
                if (currValue < mDefaultValue) {
                    mThumbOffset = (((mSeekLength - defaultAreaLength) * (float) currValue) / (float) mMaxValue);
                } else if (currValue > mDefaultValue) {
                    mThumbOffset = ((((mSeekLength - defaultAreaLength) * (float) currValue) / (float) mMaxValue) + (mDefaultAreaRadius * 2.0f));
                } else {
                    mThumbOffset = mNailOffset;
                }
            }
        }
        float top = (float) (getMeasuredHeight() / 0x2) - (mLineWidth / 2.0f);
        float bottom = top + mLineWidth;
        float right1 = ((mSeekLineStart + mNailOffset) + (mNailStrokeWidth / 2.0f)) - mNailRadius;
        if (right1 > mSeekLineStart) {
            canvas.drawRect(mSeekLineStart, top, right1, bottom, mLinePaint1);
        }
        float left2 = right1 + (mNailRadius * 2.0f);
        if (mSeekLineEnd > left2) {
            canvas.drawRect(left2, top, mSeekLineEnd, bottom, mLinePaint2);
        }

        float nailX = mSeekLineStart + mNailOffset;
        float nailY = (float) (getMeasuredHeight() / 0x2);
        if (mNailRadius > 0) {
            canvas.drawCircle(nailX, nailY, mNailRadius, mNailPaint);
        } else {
            mNailPaint.setStyle(Paint.Style.FILL);
            path.reset();
            path.moveTo(nailX, nailY - mThumbRadius / 4);
            path.lineTo(nailX + mThumbRadius / 3, nailY - mThumbRadius * 5 / 8);
            path.lineTo(nailX - mThumbRadius / 3, nailY - mThumbRadius * 5 / 8);
            path.lineTo(nailX, nailY - mThumbRadius / 4);
            canvas.drawPath(path, mNailPaint);
        }
        float thumbX = mSeekLineStart + mThumbOffset;
        float thumbY = (float) (getMeasuredHeight() / 0x2);
        float highLightLeft = thumbX + mThumbRadius;
        float highLightRight = nailX - mNailRadius;
        if (thumbX > nailX) {
            highLightLeft = nailX + mNailRadius;
            highLightRight = thumbX - mThumbRadius;
        }
        canvas.drawLine(highLightLeft, top, highLightRight, bottom, mHighLightLinePaint);
        canvas.drawCircle(thumbX, thumbY, mThumbRadius, mThumbPaint);
        mCircleRect.top = (int) (thumbY - mThumbRadius);
        mCircleRect.left = (int) (thumbX - mThumbRadius);
        mCircleRect.right = (int) (mThumbRadius + thumbX);
        mCircleRect.bottom = (int) (mThumbRadius + thumbY);

        if (mScroller.computeScrollOffset()) {
            mThumbOffset = (float) mScroller.getCurrY();
            invalidate();
        }
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if (!mIsGlobalDrag) {
                mIsTouchCircle = mCircleRect.contains((int) event.getX(), (int) event.getY());
            }
        }
        if ((!mIsGlobalDrag) && (!mIsTouchCircle)) {
            return true;
        }
        if (mEnableTouch) {
            if (!mGestureDetector.onTouchEvent(event)) {
                if ((0x1 == event.getAction()) || (0x3 == event.getAction())) {
                    mIsTouchCircle = false;
                    mGestureListener.onUp(event);
                    if (mListener != null) {
                        mListener.onSeekStopped(((float) (mCurrentValue + mStartValue) * mStep), mStep);
                    }
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    private boolean isPointInsideView(float x, float y, View view) {
        Rect viewArea = new Rect();
        view.getGlobalVisibleRect(viewArea);
        return viewArea.contains((int) x, (int) y);
    }

    public void setLineColor(String color) {
        mHighLightLinePaint.setColor(Color.parseColor(color));
        mNailPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setBaseLineColor(String color) {
        mLinePaint1.setColor(Color.parseColor(color));
        mLinePaint2.setColor(Color.parseColor(color));
    }

    public void setThumbColor(String color) {
        mThumbPaint.setColor(Color.parseColor(color));
    }

    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        super.setEnabled(enabled);
        mEnableTouch = enabled;
        if (mSavedColors == null) {
            mSavedColors = new TreeMap<String, Integer>();
        }
        if (enabled) {
            int color = (Integer) mSavedColors.get("mNailPaint").intValue();
            mNailPaint.setColor(color);
            color = (Integer) mSavedColors.get("mThumbPaint").intValue();
            mThumbPaint.setColor(color);
            color = (Integer) mSavedColors.get("mLinePaint1").intValue();
            mLinePaint1.setColor(color);
            color = (Integer) mSavedColors.get("mLinePaint2").intValue();
            mLinePaint2.setColor(color);
            color = (Integer) mSavedColors.get("mHighLightLinePaint").intValue();
            mHighLightLinePaint.setColor(color);
            return;
        }

        int seekBarColor =
            MethodChecker.getColor(
                getContext(),
                com.tokopedia.imagepicker.R.color.imagepicker_dms_seek_bar
            );

        mSavedColors.put("mNailPaint", Integer.valueOf(mNailPaint.getColor()));
        mSavedColors.put("mThumbPaint", Integer.valueOf(mThumbPaint.getColor()));
        mSavedColors.put("mLinePaint1", Integer.valueOf(mLinePaint1.getColor()));
        mSavedColors.put("mLinePaint2", Integer.valueOf(mLinePaint2.getColor()));
        mSavedColors.put("mHighLightLinePaint", Integer.valueOf(mHighLightLinePaint.getColor()));
        mNailPaint.setColor(seekBarColor);
        mThumbPaint.setColor(seekBarColor);
        mLinePaint1.setColor(seekBarColor);
        mLinePaint2.setColor(seekBarColor);
        mHighLightLinePaint.setColor(seekBarColor);
    }

    public void setThumbSize(float size) {
        mThumbRadius = size;
    }

    public void setSeekLength(int startValue, int endValue, int circleValue, float step) {
        mDefaultValue = Math.round(((float) (circleValue - startValue) / step));
        mMaxValue = Math.round(((float) (endValue - startValue) / step));
        mStartValue = Math.round(((float) startValue / step));
        mStep = step;
    }

    public void setDefaultValue(float value) {
        mCurrentValue = (Math.round((value / mStep)) - mStartValue);
        if (mDefaultListener != null) {
            mDefaultListener.onSeekDefaulted(value);
        }
        updateThumbOffset();
        invalidate();
    }

    public float getValue() {
        return ((float) (mCurrentValue + mStartValue) * mStep);
    }

    public void setValue(float value) {
        int newValue = Math.round((value / mStep)) - mStartValue;
        if (newValue == mCurrentValue) {
            return;
        }
        int previousValue = mCurrentValue;
        mCurrentValue = newValue;
        if (mListener != null) {
            mListener.onSeekChanged(previousValue * value, (mStep * value), mStep);
        }
        updateThumbOffset();
        postInvalidate();
    }

    public void setOnSeekChangeListener(OnSeekChangeListener listener) {
        mListener = listener;
    }

    public OnSeekChangeListener getOnSeekChangeListener() {
        return mListener;
    }

    public void setOnDefaultListener(OnSeekDefaultListener listener) {
        mDefaultListener = listener;
    }

    public void setOnSeekDownListener(OnSeekDownListener listener) {
        mDownListener = listener;
    }

    private void setValueInternal(int value) {
        if (mCurrentValue == value) {
            return;
        }
        int previousValue = mCurrentValue;
        mCurrentValue = value;
        if (mListener != null) {
            mListener.onSeekChanged(((float) (mStartValue + previousValue) * mStep), ((float) (mStartValue + value) * mStep), mStep);
        }
    }

    private void updateThumbOffset() {
        if ((mDefaultValue == 0) || (mDefaultValue == mMaxValue)) {
            if (mCurrentValue <= 0) {
                mThumbOffset = 0.0f;
                return;
            }
            if (mCurrentValue == mMaxValue) {
                mThumbOffset = (mSeekLineEnd - mSeekLineStart);
                return;
            }
            if (mCurrentValue == mDefaultValue) {
                mThumbOffset = mNailOffset;
                return;
            }
            mThumbOffset = (((float) mCurrentValue * mSeekLength) / (float) mMaxValue);
            return;
        }
        float defaultAreaLength = mDefaultAreaRadius * 2.0f;
        if (mCurrentValue <= 0) {
            mThumbOffset = 0.0f;
            return;
        }
        if (mCurrentValue == mMaxValue) {
            mThumbOffset = (mSeekLineEnd - mSeekLineStart);
            return;
        }
        if (mCurrentValue < mDefaultValue) {
            mThumbOffset = (((mSeekLength - defaultAreaLength) * (float) mCurrentValue) / (float) mMaxValue);
            return;
        }
        if (mCurrentValue > mDefaultValue) {
            mThumbOffset = ((((mSeekLength - defaultAreaLength) * (float) mCurrentValue) / (float) mMaxValue) + defaultAreaLength);
            return;
        }
        mThumbOffset = mNailOffset;
    }

    public void reset() {
        mSeekLength = 0.0f;
        mSeekLineStart = 0.0f;
        mSeekLineEnd = 0.0f;
        mNailOffset = 0.0f;
        mThumbOffset = 0.0f;
        mMaxValue = 0x0;
        mCurrentValue = 0x7fffffff;
        mDefaultValue = 0x0;
        mStartValue = 0x0;
        mStep = 0.0f;
        mScroller.abortAnimation();
    }

    class SeekBarGestureListener extends GestureDetector.SimpleOnGestureListener {


        public boolean onUp(MotionEvent e) {
            float initThumbOffset = mThumbOffset;
            updateThumbOffset();
            mScroller.startScroll(0x0, Math.round(initThumbOffset), 0x0, Math.round((mThumbOffset - initThumbOffset)), 0x0);
            mThumbOffset = initThumbOffset;
            invalidate();
            return true;
        }

        public boolean onDown(MotionEvent e) {
            if (mDownListener != null) {
                mDownListener.onSeekDown();
            }
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mThumbOffset -= distanceX;
            if (mThumbOffset < mSeekLineStart - mThumbRadius - getPaddingLeft()) {
                mThumbOffset = mSeekLineStart - mThumbRadius - getPaddingLeft();
            }
            if (mThumbOffset > mSeekLineEnd - mThumbRadius - getPaddingLeft()) {
                mThumbOffset = mSeekLineEnd - mThumbRadius - getPaddingLeft();
            }
            float newValue;
            if (mThumbOffset < mNailOffset - mDefaultAreaRadius) {
                newValue = mThumbOffset * (-2 + mMaxValue) / (mSeekLength - 2.0f * mDefaultAreaRadius);
            } else if (mThumbOffset > mNailOffset + mDefaultAreaRadius) {
                newValue = 1.0F + (mDefaultValue + (mThumbOffset - mNailOffset - mDefaultAreaRadius) * (-2 + mMaxValue) / (mSeekLength - 2.0f * mDefaultAreaRadius));
            } else {
                newValue = mDefaultValue;
            }
            if ((mDefaultValue == 0) || (mDefaultValue == mMaxValue)) {
                newValue = mThumbOffset * mMaxValue / mSeekLength;
            }
            if (newValue < 0.0f) {
                newValue = 0.0f;
            }
            if (newValue > mMaxValue) {
                newValue = mMaxValue;
            }
            setValueInternal(Math.round(newValue));
            invalidate();
            return true;
        }

        public boolean onSingleTapUp(MotionEvent e) {
            if (!mSupportSingleTap) {
                return false;
            }
            int newValue = mCurrentValue - 1;
            if (e.getX() > mThumbOffset) {
                newValue = mCurrentValue + 1;
            }
            if (newValue < 0) {
                newValue = 0;
            }
            if (newValue > mMaxValue) {
                newValue = mMaxValue;
            }
            setValueInternal(Math.round(newValue));
            float initThumbOffset = mThumbOffset;
            updateThumbOffset();
            mScroller.startScroll(0x0, Math.round(initThumbOffset), 0x0, Math.round((mThumbOffset - initThumbOffset)), 0x190);
            mThumbOffset = initThumbOffset;
            postInvalidate();
            if (mListener != null) {
                mListener.onSeekStopped(((float) (mCurrentValue + mStartValue) * mStep), mStep);
            }
            return true;
        }
    }

    public void setIsGlobalDrag(boolean mIsGlobalDrag) {
        this.mIsGlobalDrag = mIsGlobalDrag;
    }

    public static abstract interface OnSeekDefaultListener {
        public abstract void onSeekDefaulted(float value);
    }

    public static abstract interface OnSeekDownListener {
        public abstract void onSeekDown();
    }

    public static abstract interface OnSeekChangeListener {

        public abstract void onSeekChanged(float previousValue, float value, float step);

        public abstract void onSeekStopped(float value, float step);
    }
}
