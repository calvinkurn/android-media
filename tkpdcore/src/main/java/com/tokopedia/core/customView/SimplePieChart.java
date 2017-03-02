package com.tokopedia.core.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tkpd_Eka on 7/8/2015.
 */
public class SimplePieChart extends View{

    private Paint baseColor;
    private Paint topColor;
    private Paint capColor;
    private Paint borderColor;

    private RectF arcFilled;

    private float startDegree = 90;
    private float sweepAngle = 0;


    public SimplePieChart(Context context) {
        super(context);
        initPie();
    }

    public SimplePieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPie();
    }

    public SimplePieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPie();
    }

    public void setBaseColor(int color){
        baseColor = newPaint(color);
    }

    public void setTopColor(int color){
        topColor = newPaint(color);
    }

    public void setStartDegree(float degree){
        startDegree = degree;
        invalidate();
    }

    public void setPieFilled(float percentage){
        sweepAngle = convertPercentageToArc(percentage);
        invalidate();
    }

    private float convertPercentageToArc(float percentage){
        float arc = 0;
        arc = 360 * (percentage / 100);
        return arc;
    }

    private void initPie(){
        baseColor = newPaint(Color.LTGRAY);
        capColor = newPaint(Color.WHITE);
        topColor = newPaint(Color.GREEN);
        borderColor = newPaint(Color.WHITE);
        arcFilled = new RectF();
    }

    private Paint newPaint(int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        arcFilled.set(0, 0, getWidth(), getWidth());
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, baseColor);
        canvas.drawArc(arcFilled, startDegree, sweepAngle, true, topColor);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 3, capColor);
        canvas.drawArc(arcFilled, (startDegree)-0.5f, 1, true, borderColor);
        canvas.drawArc(arcFilled, (startDegree + sweepAngle)-0.5f, 1, true, borderColor);
    }

}
