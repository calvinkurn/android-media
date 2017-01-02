package com.tokopedia.sellerapp.gmstat.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 10/5/16.
 */
public class NChart extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private int mHeight;
    private float mHCoordinate;
    private List<NExcel> mExcels = new ArrayList<>();

    /**
     * abscissa message size
     */
    private float mAbscissaMsgSize;
    private int mWidth;
    private float mHeightestTest;
    private float mBarWidth = 0;
    /**
     * Histogram Selected color
     */
    private int mNormalColor = Color.parseColor("#42b549");
    private int mInterval;
    private Path pathLine = new Path();
    private float mTextSize = 15;
    private float mAbove;
    private float mTextMargin;

    private float ratio = 1;
    private Interpolator mInterpolator = new DecelerateInterpolator();

    /**
     * Draws abscissa information
     */
    private Paint mAbscissaPaint;
    private int mAbscissaMsgColor = Color.parseColor("#556A73");

    /**
     * Draws for line
     */
    private Paint mLinePaint;

    private float mDownX;
    private int mTouchSlop;

    private float phase = 0;//
    private Paint mDotPaint;
    private Paint mDotBgPaint;

    // Draws for vertical line
    Paint mVerticalLinePaint;

    // Tokopedia line property
    int tokopediColor = Color.parseColor("#42b549");
    int strokeWidthColor = 4;

    PointF prevMidPointF = null;
    PointF startPointF = new PointF();

    PointF nextMidPointF = null;
    PointF endPointF = new PointF();

    public NChart(Context context) {
        super(context);
        initData(context);
    }

    public NChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public NChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {
        mBarWidth = dip2px(context, 36);
        mInterval = dip2px(context, 20);
        mAbscissaMsgSize = dip2px(context, 15);
        mAbove = dip2px(context, 5);

        mAbscissaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAbscissaPaint.setTextSize(mAbscissaMsgSize);
        mAbscissaPaint.setColor(mAbscissaMsgColor);
        mAbscissaPaint.setTextAlign(Paint.Align.CENTER);//The brush text is centered

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(dip2px(context, strokeWidthColor));
        mLinePaint.setStyle(Paint.Style.STROKE);//Draw the line when the brush must be style set as Paint.Style.STROKE
        mLinePaint.setColor(mNormalColor);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        // paint for circle indicator
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStrokeWidth(dip2px(context, strokeWidthColor));

        // paint for bg circle indicator
        mDotBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotBgPaint.setStrokeWidth(dip2px(context, strokeWidthColor * 2));

        // paint for line indicator
        mVerticalLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVerticalLinePaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pathLine.reset();

        drawPolyLine(canvas);
    }

    private void drawPolyLine(Canvas canvas) {
        int pointSize = mExcels.size();
        int lastIndex = pointSize - 1;
        for (int i = 0; i < mExcels.size() - 1; i++) {

            NExcel nExcel = mExcels.get(i);
            NExcel nExcel1 = mExcels.get(i + 1);// the next point

            prevMidPointF = nExcel.getMidPointF();

            float x = prevMidPointF.x;
            float y = mHeight / 2f + (prevMidPointF.y - mHeight / 2f) * ratio + mTextMargin + mTextSize + +mAbscissaMsgSize;
            startPointF.set(x, y);

            // prevent unnecessary drawing
            if (x < -400)
                continue;

            nextMidPointF = nExcel1.getMidPointF();

            float x1 = nextMidPointF.x;
            float y1 = mHeight / 2f + (nextMidPointF.y - mHeight / 2f) * ratio + mTextMargin + mTextSize + +mAbscissaMsgSize;
            endPointF.set(x1, y1);

            // prevent unnecessary drawing
            if (x1 > mWidth + 600)
                break;

            // if finish animation.
            if (ratio == 1) {
//                drawShadePoint(canvas, x, y, "" + nExcel.getUpper());
                canvas.drawLine(x, +mAbscissaMsgSize + mTextMargin + mTextSize + dip2px(getContext(), 5), x, mHeight, mVerticalLinePaint);

                if (mExcels.size() - 1 == i + 1) {
                    drawShadePoint(canvas, x1, y1, "y");
                    canvas.drawLine(x1, +mAbscissaMsgSize + mTextMargin + mTextSize + dip2px(getContext(), 5), x1, mHeight, mVerticalLinePaint);
                }
            }

            if (pathLine.isEmpty()) {
                pathLine.moveTo(x, y);
                pathCubicTo(pathLine, startPointF, endPointF);
            } else {
//                pathLine.lineTo(x, y);
                Log.d("MNORMANSYAH", "i " + i + " x " + x + " y " + y);
                pathCubicTo(pathLine, startPointF, endPointF);

                if (i + 1 == lastIndex) {
                    pathLine.quadTo(x1, y1, x1, y1);
                }
            }

            // abcissa information of histogram
            drawAbscissaMsg(canvas, nExcel);
            drawAbscissaMsg(canvas, nExcel1);
        }
        mLinePaint.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);
//        pathEffect();
        canvas.drawPath(pathLine, mLinePaint);

        if (ratio == 1) {
            points.clear();
            selectedMaps.clear();
            getPoints();

            for(int i = 0; i < selectedMaps.size(); i++) {
                int key = selectedMaps.keyAt(i);
                // get the object by the key.
                PointF pointF = selectedMaps.get(key);
                drawShadePoint(canvas, pointF.x, pointF.y, "y");
            }
        }
    }

    SparseArrayCompat<PointF> selectedMaps = new SparseArrayCompat<>();
    List<PointF> selectedPoint = new ArrayList<>();
    final int LINE_TO_CALCULATE = 1000;

    private PointF[] getPoints() {
        PointF[] pointArray = new PointF[LINE_TO_CALCULATE];
        PathMeasure pm = new PathMeasure(pathLine, false);
        float length = pm.getLength();
        float distance = 0f;
        float speed = length / LINE_TO_CALCULATE;
        int counter = 0;
        float[] aCoordinates = new float[2];


        int pointCounter = 0;

        while ((distance < length) && (counter < LINE_TO_CALCULATE)) {
            // get point from the path
            pm.getPosTan(distance, aCoordinates, null);

            if (mExcels.get(pointCounter).getMidPointF().x <= aCoordinates[0]) {
                pointCounter++;
                selectedMaps.put((int) mExcels.get(pointCounter).getMidPointF().x,
                        new PointF(aCoordinates[0],
                                aCoordinates[1]));
                selectedPoint.add(new PointF(aCoordinates[0],
                        aCoordinates[1]));
            }

            pointArray[counter] = new PointF(aCoordinates[0],
                    aCoordinates[1]);
            counter++;
            distance = distance + speed;
        }

        return pointArray;
    }

    /**
     * The curve from prePoint to nextPoint
     *
     * @param pathline
     * @param prePoint
     * @param nextPointF
     */
    public static void pathCubicTo(Path pathline, PointF prePoint, PointF nextPointF) {

        float pointX = (prePoint.x + nextPointF.x) / 2;
        float pointY = (prePoint.y + nextPointF.y) / 2;
        Log.d("MNORMANSYAH", "x " + pointX + " y " + pointY);

        float controlX = prePoint.x;
        float controlY = prePoint.y;

        pathline.quadTo(controlX, controlY, pointX, pointY);

//        float c_x = ( prePoint.x+nextPointF.x )/2;
//        pathline.cubicTo(c_x, prePoint.y, c_x, nextPointF.y, nextPointF.x, nextPointF.y);
    }

//    public void pathCubicTo(Canvas canvas, Path pathline, PointF prePoint, PointF nextPointF){
//
//        float pointX = (prePoint.x + nextPointF.x) / 2;
//        float pointY = (prePoint.y + nextPointF.y) / 2;
//
//        pathCubicTo(pathline, prePoint, nextPointF);
//
//        drawShadePoint(canvas, pointX, pointY, "" + "");
//    }

    /**
     * Draw small dots
     *
     * @param canvas
     * @param endx
     * @param endy
     */
    private void drawShadePoint(Canvas canvas, float endx, float endy, String text) {
        /**
         * Draws the last data point
         */
        switch (text) {
//            case "":
//                mDotPaint.setColor(getColor(getContext(), android.R.color.black));
//                break;
//            case "x":
//                mDotPaint.setColor(getColor(getContext(), android.R.color.holo_blue_bright));
//                break;
//            case "y":
//                mDotPaint.setColor(getColor(getContext(), android.R.color.holo_purple));
//                break;
            default:
                mDotPaint.setColor(tokopediColor);
                break;
        }

        mDotBgPaint.setColor(getColor(getContext(), android.R.color.white));
        canvas.drawCircle(endx, endy, dip2px(getContext(), strokeWidthColor * 2), mDotBgPaint);

        canvas.drawCircle(endx, endy, dip2px(getContext(), strokeWidthColor), mDotPaint);
        mDotPaint.setAlpha(125);
    }

    private void drawAbscissaMsg(Canvas canvas, NExcel nExcel) {
        mAbscissaPaint.setColor(mAbscissaMsgColor);
        PointF midPointF = nExcel.getMidPointF();
//        canvas.drawText(nExcel.getXmsg(), midPointF.x, mHCoordinate + mTextMargin + mTextSize, mAbscissaPaint);
        canvas.drawText(nExcel.getXmsg(), midPointF.x, mTextMargin + mTextSize + mAbscissaMsgSize, mAbscissaPaint);
    }

    /**
     * so it is effect for path drawing.
     */
    private void pathEffect() {
        DashPathEffect pathEffect = new DashPathEffect(new float[]{10, 8, 5, 10}, phase);
        phase = ++phase % 50;//Changing the dashed line moving effect

        mLinePaint.setPathEffect(pathEffect);
        invalidate();
    }

    List<Pair<PointF, PointF>> points = new ArrayList<>();


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;

        // set shader for vertical line
        LinearGradient linearGradient = new LinearGradient(0, mHeight, 0, 0, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR);
        mVerticalLinePaint.setShader(linearGradient);

        // calculate coordinate for chart to draw
        mHCoordinate = mHeight - Math.abs(2 * mAbscissaMsgSize);

        if (mExcels.size() > 0) {
            scaleHeight();
        }
        if (mExcels.size() > 0) {
            animateExcels();
        }

    }

    private void scaleHeight() {
        if (mHCoordinate > 0) {
            // determine ratio based on highest pillar
            /*
      The radius of the polyline
     */
            float mLinePointRadio = 0;
            float mHeightRatio = (mHCoordinate - 2 * mTextSize - mAbove - mTextMargin - mLinePointRadio - 5) / mHeightestTest;

            for (int i = 0; i < mExcels.size(); i++) {
                //[START] scale the height
                NExcel nExcel = mExcels.get(i);
                nExcel.setHeight(nExcel.getHeight() * mHeightRatio);
                nExcel.setWidth(mBarWidth);
                //[END] scale the height

                //[START] currently this is unused code
                PointF start = nExcel.getStart();
                start.x = mInterval * (i + 1) + mBarWidth * i;
                start.y = mHCoordinate - mAbove - nExcel.getLower();
                //[START] currently this is unused code
            }
        }
    }

    public void setBarStanded() {

    }

    public void setNormalColor() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mExcels.size() > 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    boolean moved;
                    float mSliding;
                    if (ratio == 1) {//Do not slide while animating
                        float moveX = event.getX();
                        mSliding = moveX - mDownX;
                        if (Math.abs(mSliding) > mTouchSlop) {
                            //          pathLine.reset();
                            if (mExcels.get(0).getStart().x + mSliding > mInterval || mExcels.get(mExcels.size() - 1)
                                    .getStart().x + mBarWidth + mInterval + mSliding < mWidth) {
                                return true;
                            }
                            for (int i = 0; i < mExcels.size(); i++) {
                                NExcel excel = mExcels.get(i);
                                PointF start = excel.getStart();
                                start.x += mSliding;//The chart moves left and right
                            }
                            boolean mScrollAble = true;
                            if (mScrollAble) {
                                invalidate();
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    /*if (!moved) {
                        PointF tup = new PointF(event.getX(), event.getY());
                        mSelected = clickWhere(tup);
                        invalidate();
                    }*/
                    break;
            }
        }
        return true;
    }

    /**
     * TODO This code has 3 iteration,
     * 1. search for the highest
     * 2. set bar width ( for bar ony ), set point start for x coordinate
     * and set color also in here.
     * 3. set path from data's point
     * 4.
     *
     * @param nExcelList
     */
    public void cmdFill(List<NExcel> nExcelList) {
        // hapus data
        mExcels.clear();

        // TODO search the highest can be faster
        // search the highest height
        for (NExcel nExcel : nExcelList) {
            mHeightestTest = mHeightestTest > nExcel.getHeight() ? mHeightestTest : nExcel.getHeight();
        }

        for (int i = 0; i < nExcelList.size(); i++) {

            //[START] add to data structure
            NExcel nExcel = nExcelList.get(i);
            nExcel.setWidth(mBarWidth);
            PointF start = nExcel.getStart();
            start.x = mInterval * (i + 1) + mBarWidth * i;
            nExcel.setColor(mNormalColor);
            mExcels.add(nExcel);
            //[END] add to data structure

            //[START] set the path point
            /*PointF midPointF = nExcel.getMidPointF();
            if(i==0){
                pathLine.moveTo(midPointF.x, midPointF.y);
            }else{
                pathLine.lineTo(midPointF.x, midPointF.y);
            }*/
            //[END] set the path point
        }

        if (mWidth != 0) {
            scaleHeight();
            postInvalidate();
        }

    }

    private void animateExcels() {
        long animateTime = 1600;
        ValueAnimator mVa = ValueAnimator.ofFloat(0, 1).setDuration(animateTime);
        mVa.addUpdateListener(this);
        mVa.setInterpolator(mInterpolator);
        mVa.addListener(this);
        mVa.start();
        Log.d("NChart", "animateExcels() ");
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        ratio = (float) animation.getAnimatedValue();
        postInvalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public static int getColor(Context context, @ColorRes int resColorId){
        return ContextCompat.getColor(context, resColorId);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}