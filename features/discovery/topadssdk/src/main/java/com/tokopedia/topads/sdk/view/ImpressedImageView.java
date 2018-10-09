package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.listener.ImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import android.view.ViewTreeObserver;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class ImpressedImageView extends AppCompatImageView {

    private static final String TAG = ImpressedImageView.class.getSimpleName();
    private ProductImage image;
    private boolean execute;

    public ImpressedImageView(Context context) {
        super(context);
        registerObserver(this);
    }

    public ImpressedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerObserver(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        registerObserver(this);
    }

    private void registerObserver(View view){
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(isVisible(view) && image!=null && !image.isImpressed()){
                    new ImpresionTask().execute(image.getM_url());
                    image.setImpressed(true);
                }
            }
        });
    }

    public static boolean isVisible(final View view) {
        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }
        final Rect actualPosition = new Rect();
        view.getGlobalVisibleRect(actualPosition);
        final Rect screen = new Rect(0, 0, getScreenWidth(), getScreenHeight());
        return actualPosition.intersect(screen);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void setImage(ProductImage image) {
        this.image = image;
        Glide.with(getContext()).load(image.getM_ecs()).into(this);
    }
}