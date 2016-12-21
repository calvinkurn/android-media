package com.tokopedia.sellerapp.gmstat.utils;

import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

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
import com.db.chart.tooltip.Tooltip;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.tokopedia.sellerapp.gmstat.views.YAxisRenderer;

import java.text.DecimalFormat;

/**
 * Created by normansyahputa on 11/24/16.
 */

public class WilliamChartUtils {
    private String[] mLabels;
    private float[] mValues;
    private static boolean mIsLineSmooth = true;
    private static float mLineThickness = 3 ;
    private static int mLineColorId;
    private static float mPointsSize = 4;
    private static int mPointColorId;
    private static Paint mGridPaint;
    private static int mGridColorId;
    private static float mGridThickness;
    private static boolean mHasXAxis = true;
    private static AxisRenderer.LabelPosition mXLabelPosition;
    private static boolean mHasYAxis = true;
    private static AxisRenderer.LabelPosition mYLabelPosition;
    private static int mLabelColorId;
    private static int mAxisColorId;
    private static ChartView.GridType mGridType = ChartView.GridType.VERTICAL;
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
    private Drawable dotDrawable;
    private Tooltip tooltip;

    public WilliamChartUtils setDotDrawable(Drawable dotDrawable) {
        this.dotDrawable = dotDrawable;
        return this;
    }

    public WilliamChartUtils setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public WilliamChartUtils(String[] mLabels, float[] mValues){
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
        mXLabelPosition = AxisRenderer.LabelPosition.OUTSIDE;
        mYLabelPosition = AxisRenderer.LabelPosition.OUTSIDE;
        mEasingId = 0;
        mOverlapFactor = 1;
        mOverlapOrder = mEqualOrder;
        mStartX = 0f;
        mStartY = 1f;
    }

    private final static int[] mEqualOrder = {0, 1, 2, 3, 4, 5, 6};

    public WilliamChartUtils setmLabels(String[] mLabels) {
        this.mLabels = mLabels;
        return this;
    }

    public WilliamChartUtils setmValues(float[] mValues) {
        this.mValues = mValues;
        return this;
    }

    public LineChartView buildLineChart(LineChartView chart) {

        chart.reset();

        LineSet dataset = new LineSet(mLabels, mValues);

//        if (mIsLineDashed) dataset.setDashed(mLineDashType);
        dataset.setSmooth(mIsLineSmooth)
                .setThickness(Tools.fromDpToPx(mLineThickness))
                .setColor(mLineColorId);

        dataset.setDotsRadius(Tools.fromDpToPx(mPointsSize)).setDotsColor(mPointColorId);
        chart.addData(dataset);

        return chart;
    }

    public void buildChart(LineChartView chart) {

        // Tooltip
        Tooltip mTip = tooltip;

//        ((TextView) mTip.findViewById(R.id.value)).setTypeface(
//                Typeface.createFromAsset(chart.getContext().getAssets(), "OpenSans-Semibold.ttf"));

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(58), (int) Tools.fromDpToPx(25));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

            mTip.setPivotX(Tools.fromDpToPx(65) / 2);
            mTip.setPivotY(Tools.fromDpToPx(25));
        }

        chart.setTooltips(mTip);
        chart.putYRndrStringFormatter(new YAxisRenderer());
        chart.setDrawable(dotDrawable);

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
