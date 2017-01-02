package com.tokopedia.sellerapp.gmstat.utils;

import android.graphics.Color;
import android.graphics.Paint;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.animation.easing.BaseEasingMethod;
import com.db.chart.animation.easing.BounceEase;
import com.db.chart.animation.easing.CircEase;
import com.db.chart.animation.easing.CubicEase;
import com.db.chart.animation.easing.ElasticEase;
import com.db.chart.animation.easing.ExpoEase;
import com.db.chart.animation.easing.LinearEase;
import com.db.chart.animation.easing.QuadEase;
import com.db.chart.animation.easing.QuartEase;
import com.db.chart.animation.easing.QuintEase;
import com.db.chart.animation.easing.SineEase;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;

import java.text.DecimalFormat;

/**
 * Created by normansyahputa on 12/17/16.
 */

public class WiliamChartUtils_ {
    private static String[] mLabels;
    private static float[] mValues;
    private static boolean mIsLineSmooth = true;
    private static float mLineThickness = 3 ;
    private static int mLineColorId;
    private static float mPointsSize = 0;
    private static int mPointColorId;
    private static Paint mGridPaint;
    private static int mGridColorId;
    private static float mGridThickness;
    private static boolean mHasXAxis = false;
    private static AxisRenderer.LabelPosition mXLabelPosition;
    private static boolean mHasYAxis = false;
    private static AxisRenderer.LabelPosition mYLabelPosition;
    private static int mLabelColorId;
    private static int mAxisColorId;
    private static ChartView.GridType mGridType = ChartView.GridType.NONE;
    private static String mLabelFormat = "";
    private static int mEasingId;
    private static BaseEasingMethod mEasing;
    private static int mDuration = 500;
    private static int mAlpha = 1;
    private static int[] mOverlapOrder;
    private static float mOverlapFactor;
    private static float mStartX;
    private static float mStartY;
    private static final Runnable mEndAction = new Runnable() {
        @Override
        public void run() {

//            mPlayBtn.setEnabled(true);
        }
    };

    private static final int GREEN_COLOR = Color.rgb(66,181,73);
    private static final int GREY_COLOR = Color.rgb(189,189,189);


    public WiliamChartUtils_(String[] mLabels, float[] mValues){
        if(mLabels == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");
        if(mValues==null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");

        this.mLabels = mLabels;
        this.mValues = mValues;

        mLineColorId = Color.rgb(66,181,73);
        mPointColorId = Color.rgb(255,255,255);
        mGridColorId = Color.argb(13, 0,0,0);
        mLabelColorId = Color.argb(97, 0,0,0);
        mAxisColorId = Color.argb(13, 0,0,0);
        mGridThickness = 1f;
        mXLabelPosition = AxisRenderer.LabelPosition.NONE;
        mYLabelPosition = AxisRenderer.LabelPosition.NONE;
        mEasingId = 0;
        mOverlapFactor = 1;
        mOverlapOrder = mEqualOrder;
        mStartX = 0f;
        mStartY = 1f;
    }

    private static int[] mEqualOrder = {0, 1, 2, 3, 4, 5, 6};

    public static void setmLabels(String[] mLabels) {
        WiliamChartUtils_.mLabels = mLabels;
    }

    public static void setmValues(float[] mValues) {
        WiliamChartUtils_.mValues = mValues;

        if(mValues.length != mEqualOrder.length)
        {
            mEqualOrder = new int[mValues.length];
            for(int i=0;i<mEqualOrder.length;i++){
                mEqualOrder[i] = i;
            }
        }
        mOverlapOrder = mEqualOrder;
    }

    public ChartView buildLineChart(LineChartView chart, int bottomMargin, boolean emptyState) {

        if(emptyState){
            mLineColorId = GREY_COLOR;
        }else{
            mLineColorId = GREEN_COLOR;
        }

        chart.reset();
        chart.resetYRndr();

        LineSet dataset = new LineSet(mLabels, mValues);

//        if (mIsLineDashed) dataset.setDashed(mLineDashType);
        dataset.setSmooth(LineSet.SMOOTH_QUAD)
                .setThickness(Tools.fromDpToPx(mLineThickness))
                .setColor(mLineColorId);

        dataset.setDotsRadius(Tools.fromDpToPx(mPointsSize)).setDotsColor(mPointColorId);
        chart.addData(dataset);
        chart.setTopMargin(0);
        chart.setRightMargin(0);
        chart.setBottomMargin(bottomMargin);

        return chart;
    }

    public static void buildChart(ChartView chart) {

        mGridPaint = new Paint();
        mGridPaint.setColor(mGridColorId);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStrokeWidth(Tools.fromDpToPx(mGridThickness));
//        if (mIsGridDashed) mGridPaint.setPathEffect(new DashPathEffect(mGridDashType, 0));

        chart.setXAxis(mHasXAxis)
                .setXLabels(mXLabelPosition)
                .setYAxis(mHasYAxis)
                .setYLabels(mYLabelPosition)
                .setLabelsColor(mLabelColorId)
                .setAxisColor(mAxisColorId);

        if (mGridType != null) chart.setGrid(mGridType, mGridPaint);

        chart.setLabelsFormat(new DecimalFormat("#" + mLabelFormat));

        chart.show(buildAnimation());
    }

    private static Animation buildAnimation() {

        switch (mEasingId) {
            case 0:
                mEasing = new CubicEase();
                break;
            case 1:
                mEasing = new QuartEase();
                break;
            case 2:
                mEasing = new QuintEase();
                break;
            case 3:
                mEasing = new BounceEase();
                break;
            case 4:
                mEasing = new ElasticEase();
                break;
            case 5:
                mEasing = new ExpoEase();
                break;
            case 6:
                mEasing = new CircEase();
                break;
            case 7:
                mEasing = new QuadEase();
                break;
            case 8:
                mEasing = new SineEase();
                break;
            case 9:
                mEasing = new LinearEase();
                break;
            default:
                mEasing = new CubicEase();
        }

        return new Animation(mDuration).setAlpha(mAlpha)
                .setEasing(mEasing)
                .setOverlap(mOverlapFactor, mOverlapOrder)
                .setStartPoint(mStartX, mStartY)
                .setEndAction(mEndAction);
    }
}
