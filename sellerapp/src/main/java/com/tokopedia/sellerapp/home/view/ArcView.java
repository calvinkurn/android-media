package com.tokopedia.sellerapp.home.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by normansyahputa on 10/18/16.
 */
public class ArcView extends View {

    // Max and min progress values
    private final static float MAX_PROGRESS = 100.0F;
    private final static float MIN_PROGRESS = 0.0F;

    // Max and min end angle
    private final static float MAX_ANGLE = 360.0F;
    private final static float MIN_ANGLE = 0.0F;
    public static final String TAG = "ArcView";

    List<Model> models;
    private int mSize;

    private float mProgressModelSize;
    private float mProgressModelOffset = 3F;

    Paint paint;
    private Paint textPoint;

    // Start and end angles
    private float mStartAngle = 90F;
    private float mSweepAngle = MAX_ANGLE;

    public boolean mIsAnimated = true;
    private final ValueAnimator mProgressAnimator = new ValueAnimator();
    // Max and min fraction values
    private final static float MAX_FRACTION = 1.0F;
    private final static float MIN_FRACTION = 0.0F;
    private ValueAnimator.AnimatorListener mAnimatorListener;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mAnimationDuration = 1000;
    private float mAnimatedFraction;

    private final static int ANIMATE_ALL_INDEX = -2;
    private final static int DISABLE_ANIMATE_INDEX = -1;
    private int mActionMoveModelIndex = DISABLE_ANIMATE_INDEX;

    static final Random random = new Random();
    private Paint mProgressPaint= new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setStyle(Style.STROKE);
        }
    };
    private int textSize;

    int textColor =  Color.parseColor("#FF000000");
    int textPositionX, textPositionY;
    Rect bounds = new Rect();

    private int[] colors = {
            Color.parseColor("#42b549"),
            Color.parseColor("#98CFF4")
    };
    private Paint textPaint;
    private float value;

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static List<Model> getDefaultModels(){
        ArrayList<Model> models = new ArrayList<>();
        for(int i=0;i<20;i++) {
            float mProgress = 20 + random.nextInt(80);
            Model model = new Model(mProgress);
            models.add(model);
        }
        return models;
    }

    public static List<Model> getSameDefaultModels(){
        ArrayList<Model> models = new ArrayList<>();
        for(int i=0;i<20;i++) {
            float mProgress = 100;
            if(i > 1 && i < 21) {
                Model model = new Model(mProgress);
                models.add(model);
            }else{
                Model model = new Model(0);
                models.add(model);
            }
        }
        return models;
    }

    public static List<Model> getSameDefaultModels(int progress){
        ArrayList<Model> models = new ArrayList<>();
        for(int i=0;i<20;i++) {
            float mProgress = 100;
            if(i > 1 && i < 21) {
                Model model = new Model(progress);
                models.add(model);
            }else{
                Model model = new Model(0);
                models.add(model);
            }
        }
        return models;
    }

    public ArcView(Context context, List<Model> models) {
        super(context);
        init(context, models);
    }

    public ArcView(Context context) {
        super(context);
        init(context, new ArrayList<Model>());
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, new ArrayList<Model>());
    }

    public ArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, new ArrayList<Model>());
    }

    private void init(Context context, List<Model> models){

        this.models = models;

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        textPoint = new Paint();
        textPoint.setColor(Color.GRAY);
        textPoint.setStrokeWidth(4);
        textSize = dip2px(context, 14);
        textPoint.setTextSize(textSize);

        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                float progress = lastModel.mRawProgress * animatedValue * 100F;
//                Log.d(TAG, "animatedValue "+animatedValue+" progress "+ progress+ " lastModel.mProgress "+lastModel.mProgress);
                lastModel.mProgress = progress/100;
            }
        };

        mProgressAnimator.setFloatValues(MIN_FRACTION, MAX_FRACTION);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                mAnimatedFraction = (float) animation.getAnimatedValue();
                if (mAnimatorUpdateListener != null)
                    mAnimatorUpdateListener.onAnimationUpdate(animation);

                postInvalidate();
            }
        });

        textPaint = new Paint();
        textPaint.setColor(adjustAlpha(0xCC000000, 0.5f));
        textPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
//        textPaint.setTextSize(dip2px(context, 45));
        textPaint.setAlpha(190);


    }

    // Animate progress
    public void animateProgress() {
        if (!mIsAnimated || mProgressAnimator == null) return;
        if (mProgressAnimator.isRunning()) {
            if (mAnimatorListener != null) mProgressAnimator.removeListener(mAnimatorListener);
            mProgressAnimator.cancel();
        }
        // Set to animate all models
        mActionMoveModelIndex = ANIMATE_ALL_INDEX;
        mProgressAnimator.setDuration(mAnimationDuration);
        mProgressAnimator.setInterpolator(mInterpolator);
        if (mAnimatorListener != null) {
            mProgressAnimator.removeListener(mAnimatorListener);
            mProgressAnimator.addListener(mAnimatorListener);
        }
        mProgressAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);


        if(width > height) {
            mSize = height;
        }else {
            mSize = width;
        }

        Log.d(TAG, "width "+width+" height "+height+" ratio "+(textPaint.getTextSize()/mSize));
        textPaint.setTextSize((float) (0.28 * mSize));

        float divider = mSize * 0.35f;
        mProgressModelSize = divider / ( models.size() <= 0 ? 1 : models.size() );
        // THIS IS IMPORTANT TO MAKE CIRCLE IS GOOD ENOUGH
        final float paintOffset = mProgressModelSize * 0.5F;
        mProgressModelOffset = mProgressModelSize - 0.5f;

        Log.d(TAG, "paintOffset "+ paintOffset);

        for(int i=0;i<models.size();i++){
            Model model = models.get(i);

            float modelOffset = (mProgressModelSize * i) + paintOffset -
                    (mProgressModelOffset * i);

            Log.d(TAG, "modelOffset "+ modelOffset+" paintOffset "+ paintOffset
                    + " mProgressModelOffset "+(mProgressModelOffset * i)+" : "
                    + (mProgressModelSize * i)
            );

            // set bound to progress
            model.mBounds.set(
                    modelOffset, modelOffset, mSize-modelOffset, mSize-modelOffset
            );

            if(i == models.size()-1){
                lastModel.mRawProgress = model.mProgress;
                Log.d(TAG, "bounds "+ model.mBounds.toString());
                // calculate position of text
                String s = "100 %";
                textPaint.getTextBounds(s, 0, s.length(), bounds);
                Log.d(TAG, "text bounds "+ bounds);
                textPositionX = textPositionY = (int) ((model.mBounds.top + model.mBounds.bottom) /2);
                textPositionX -= (bounds.left/2);
                textPositionY -= (bounds.top/2);
//                textPositionX -= bounds.width();
//                textPositionY -= bounds.height();

            }
        }

        // set square measured dimension
        setMeasuredDimension(mSize, mSize);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w "+w+" h "+h);
    }

    Model lastModel = new Model(0);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Save and rotate to start angle
        canvas.save();
        final float radius = mSize * 0.5F;
        canvas.rotate(mStartAngle, radius, radius);

        for(int i =0;i < models.size();i++) {
            Model model = models.get(i);

//            canvas.drawRect(model.mBounds, paint);

//            String text = Integer.toString(i);
//            canvas.drawText(text, model.mBounds.top, model.mBounds.left+textSize, textPoint);

            float progressFraction = 0.0f;
            if(mIsAnimated) {
                progressFraction = (model.mLastProgress + (mAnimatedFraction * (model.mProgress-model.mLastProgress)))/MAX_PROGRESS;
            }else{
                progressFraction = model.mProgress / MAX_PROGRESS;
            }
            float progress = progressFraction * mSweepAngle;

            if(model.mRawProgress >= 0) {
                mProgressPaint.setColor(model.bgColor);
                mProgressPaint.setAlpha(60);
                canvas.drawArc(model.mBounds, 0.0F, 360.0F, false, mProgressPaint);
            }

//            if(i%2==0){
//                mProgressPaint.setColor(colors[1]);
//            }else{
                mProgressPaint.setColor(colors[0]);
//            }
            mProgressPaint.setStrokeWidth(mProgressModelSize);

            model.mPath.reset();
            model.mPath.addArc(model.mBounds, 0.0F, progress);
            mProgressPaint.setShader(null);
            mProgressPaint.setStyle(Paint.Style.STROKE);
            mProgressPaint.setAlpha(255);
            canvas.drawPath(model.mPath, mProgressPaint);

//            if(i==models.size()-1){
//                lastModel.mProgress = model.mProgress;
//            }
        }

        canvas.restore();
        textPaint.setTextAlign(Paint.Align.CENTER);
        String centerText = Integer.toString((int)lastModel.mProgress)+" %";
        canvas.drawText(centerText, textPositionX, textPositionY, textPaint);
//        canvas.drawRect(bounds, paint);
    }

    public void setSingleValue(float value){
        this.value = value;
        setModels(getSameDefaultModels((int) value));
    }

    public void setModels(List<Model> defaultModels) {
        this.models.addAll(defaultModels);
        requestLayout();
    }

    public static final class Model{
        RectF mBounds = new RectF();
        Path mPath = new Path();
        float mProgress;
        float mLastProgress;
        float mRawProgress;
        int bgColor = Color.GRAY;

        public Model(float mProgress) {
            setProgress(mProgress);
            mLastProgress = 0;
            mRawProgress = mProgress;
        }

        public void setProgress(@FloatRange(from = MIN_PROGRESS, to = MAX_PROGRESS) final float progress){
            mLastProgress = mProgress;
            mProgress = (int)max(MIN_PROGRESS, min(progress, MAX_PROGRESS));
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
