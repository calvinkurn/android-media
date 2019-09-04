package com.tokopedia.search.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.tokopedia.search.R;

public class CountDrawable extends Drawable {
    private Paint badgePaint;
    private Paint textPaint;
    private Rect txtRect = new Rect();

    private String count = "";
    private boolean willDraw;

    public CountDrawable(Context context) {
        createBadgePaint(context);
        createTextPaint(context);
    }

    private void createTextPaint(Context context) {
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(context.getResources().getDimension(R.dimen.sp_8));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void createBadgePaint(Context context) {
        badgePaint = new Paint();
        badgePaint.setColor(ContextCompat.getColor(context.getApplicationContext(), R.color.red_800));
        badgePaint.setAntiAlias(true);
        badgePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {

        if (!willDraw) {
            return;
        }
        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        float radius = ((Math.max(width, height) / 2)) / 2;
        float centerX = (width - radius - 1) +5;
        float centerY = radius -5;
        if(count.length() <= 2){
            canvas.drawCircle(centerX, centerY, (int)(radius+5.5), badgePaint);
        }
        else{
            canvas.drawCircle(centerX, centerY, (int)(radius+6.5), badgePaint);
        }
        textPaint.getTextBounds(count, 0, count.length(), txtRect);
        float textHeight = txtRect.bottom - txtRect.top;
        float textY = centerY + (textHeight / 2f);
        if(count.length() > 2)
            canvas.drawText("99+", centerX, textY, textPaint);
        else
            canvas.drawText(count, centerX, textY, textPaint);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(String count) {
        this.count = count;

        // Only draw a badge if there are notifications.
        willDraw = !count.equalsIgnoreCase("0");
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
