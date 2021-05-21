package com.tokopedia.homecredit.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.tokopedia.homecredit.R;

public class RectangleOverlayView extends LinearLayout {

    private Bitmap bitmap;
    private float insetDist;
    private int outLineImageId;
    private int colorId;



    public RectangleOverlayView(Context context) {
        super(context);
    }

    public RectangleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RectangleOverlayView);
        insetDist = convertPixelsToDp(
                arr.getDimension(R.styleable.RectangleOverlayView_inset_distance, 0), context);
        outLineImageId = arr.getResourceId(R.styleable.RectangleOverlayView_outline_img, -1);
        colorId = arr.getResourceId(R.styleable.RectangleOverlayView_fg_color, -1);
    }

    public RectangleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RectangleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        RectF ktpRectangle = new RectF(
                getWidth() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_width),
                getHeight() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_height),
                getWidth() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_width),
                getHeight() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_height));

        RectF ktpRectangle_2 = new RectF(
                (getWidth() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_width))+ insetDist,
                (getHeight() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_height))+insetDist,
                (getWidth() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_width))-insetDist,
                (getHeight() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_height))-insetDist);


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(colorId != -1){
            paint.setColor(getResources().getColor(colorId));
        }else {
            paint.setColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500));
        }

        paint.setAlpha(255);
        osCanvas.drawRect(mainRectangle, paint);

        if(outLineImageId != -1) {
            osCanvas.drawBitmap(drawableToBitmap(getResources().getDrawable(outLineImageId)), null, ktpRectangle, null);
        }

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        osCanvas.drawRoundRect(ktpRectangle_2, 0, 0, paint);

    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        return bitmap;
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
