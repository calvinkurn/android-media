package com.tokopedia.gamification.cracktoken.presentation.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.gamification.R;

/**
 * Created by Rizky on 28/03/18.
 */

public class CountdownTimerView extends View {

    private Paint backgroundPaint;
    private TextPaint numberPaint;

    private RectF backgroundRect;

    private int cornerRadius;

    private String textTime;

    public CountdownTimerView(Context context) {
        super(context);
        init();
    }

    public CountdownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountdownTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorBgCountdownTimer));

        numberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        numberPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        numberPaint.setTextSize(Math.round(24f * getResources().getDisplayMetrics().scaledDensity));

        backgroundRect = new RectF();

        cornerRadius = Math.round(56f * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int canvasWidth = canvas.getWidth();
        final int canvasHeight = canvas.getHeight();

        final float centerX = canvasWidth * 0.5f;

        final float textWidth = numberPaint.measureText(textTime);

        backgroundRect.set(0f, 0f, textWidth, canvasHeight);
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint);

        final float baselineY = Math.round(canvasHeight * 0.5f);

        final float textX = Math.round(centerX - textWidth * 0.5f);

        canvas.drawText(textTime, textX, baselineY, numberPaint);
    }

    public void setTime(String textTime) {
        this.textTime = textTime;
        invalidate();
    }

}