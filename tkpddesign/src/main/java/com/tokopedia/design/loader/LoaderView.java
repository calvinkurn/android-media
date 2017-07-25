package com.tokopedia.design.loader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.seller.R;

/**
 * Created by hendry on 7/14/2017.
 */

public class LoaderView extends View implements ILoaderView {
    private LoaderController loaderController;

    public LoaderView(Context context) {
        super(context);
        init(null);
    }

    public LoaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        loaderController = new LoaderController(this);
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attrs, R.styleable.loader_view, 0, 0);
        loaderController.setUseGradient(
                typedArray.getBoolean(
                        R.styleable.loader_view_use_gradient,
                        LoaderConstant.USE_GRADIENT_DEFAULT));
        typedArray.recycle();

    }

    public void resetLoader() {
        loaderController.startLoading();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        loaderController.onSizeChanged();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loaderController.onDraw(canvas);
    }

    @Override
    public void setRectColor(Paint rectPaint) {
        rectPaint.setColor(LoaderConstant.COLOR_DEFAULT_GREY);
    }

    @Override
    public boolean valueSet() {
        // never has value
        return false;
    }

    public void stopLoading() {
        if (loaderController != null) {
            loaderController.stopLoading();
        }
    }
}
