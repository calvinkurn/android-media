package com.tokopedia.design.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tokopedia.design.R;

public class CircleOverlayView extends LinearLayout {

    private Bitmap bitmap;

    public CircleOverlayView(Context context) {
        super(context);
    }

    public CircleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (bitmap == null) {
            createBitmap();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void createBitmap() {

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF mainRectangle = new RectF(0, 0, getWidth(), getHeight());

        float ovalWidth = getWidth() / 2 + getResources().getDimensionPixelSize(R.dimen.hc_oval_width);
        float ovalHeight = getHeight() / 2 - getResources().getDimensionPixelSize(R.dimen.hc_oval_height);
        float ktpRectWidth = ovalWidth - getResources().getDimensionPixelSize(R.dimen.hc_oval_width);
        float ktpRectHeight = getResources().getDimensionPixelSize(R.dimen.hc_ktp_height);


        float ovalAndKtpRectDiffHeight = getResources().getDimensionPixelSize(R.dimen.hc_oval_width);
        float outerRectWidth = ovalWidth + getResources().getDimensionPixelSize(R.dimen.hc_oval_width);
        float outerRectHeight = ovalHeight + ktpRectHeight + ovalAndKtpRectDiffHeight + getResources().getDimensionPixelSize(R.dimen.hc_oval_width);     //100px is gap b/w oval and ktprect
        float ovalVerticleShift = Math.abs((ovalHeight + ktpRectHeight + ovalAndKtpRectDiffHeight) / 2 - ovalHeight);
        RectF outerRectangle = new RectF(
                getWidth() / 2 - outerRectWidth / 2,
                getHeight() / 2 - outerRectHeight / 2,
                getWidth() / 2 + outerRectWidth / 2,
                getHeight() / 2 + outerRectHeight / 2);

        RectF innerRectangle = new RectF(
                getWidth() / 2 - ovalWidth / 2,
                getHeight() / 2 - ovalHeight + ovalVerticleShift,
                getWidth() / 2 + ovalWidth / 2,
                getHeight() / 2 + ovalVerticleShift);

        RectF ktpRectangle = new RectF(
                getWidth() / 2 - ktpRectWidth / 2,
                getHeight() / 2 + ovalAndKtpRectDiffHeight + ovalVerticleShift,
                getWidth() / 2 + ktpRectWidth / 2,
                getHeight() / 2 + ktpRectHeight + ovalAndKtpRectDiffHeight + ovalVerticleShift);


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500));

        paint.setAlpha(255);
        osCanvas.drawRect(mainRectangle, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setAlpha(49);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        osCanvas.drawOval(innerRectangle, paint);

        osCanvas.drawRoundRect(ktpRectangle, 10, 10, paint);

    }
}