package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.listener.ImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import android.view.ViewTreeObserver;

import org.jetbrains.annotations.Nullable;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class ImpressedImageView extends AppCompatImageView {

    private static final String TAG = ImpressedImageView.class.getSimpleName();
    public static final int BOTTOM_MARGIN = 50;
    private ProductImage image;
    private boolean execute;
    private ViewHintListener hintListener;

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
                    if(hintListener!=null){
                        hintListener.onViewHint();
                    }
                    image.setImpressed(true);
                }
            }
        });
    }

    private boolean isVisible(final View view) {
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

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels - offsetBottomMargin(BOTTOM_MARGIN);
    }

    public void setViewHintListener(ViewHintListener hintListener) {
        this.hintListener = hintListener;
    }

    public void setImage(ProductImage image) {
        this.image = image;
        Glide.with(getContext()).load(image.getM_ecs()).into(this);
    }

    private int offsetBottomMargin(float dp){
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public interface ViewHintListener {
        void onViewHint();
    }
}