package com.tokopedia.topads.sdk.v2.dynamicfeedshop.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.media.loader.JvmMediaLoader;
import com.tokopedia.kotlin.model.ImpressHolder;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.CpmImage;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.utils.ImpresionTask;

import android.view.ViewTreeObserver;

/**
 * @author by errysuprayogi on 3/27/17.
 */
//need to delete once feed remove dependency
public class ImpressedImageView extends AppCompatImageView {

    private ImpressHolder holder;
    private ViewHintListener hintListener;
    private int offset;
    private ViewTreeObserver.OnScrollChangedListener scrollChangedListener;
    private String className = "com.tokopedia.topads.sdk.view.ImpressedImageView";

    public ImpressedImageView(Context context) {
        super(context);
    }

    public ImpressedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (holder != null && !holder.isInvoke()) {
            invoke();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (holder != null && holder.isInvoke()) {
            revoke();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void setScrollChangedListener(ViewTreeObserver.OnScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }

    private ViewTreeObserver.OnScrollChangedListener getScrollChangedListener() {
        return scrollChangedListener;
    }

    private void revoke() {
        getViewTreeObserver().removeOnScrollChangedListener(getScrollChangedListener());
    }

    private void invoke() {
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                setScrollChangedListener(this);
                if (isVisible(getView())) {
                    if (holder != null && !holder.isInvoke()) {
                        if(hintListener!=null){
                            hintListener.onViewHint();
                        }
                        if(holder instanceof ProductImage){
                            new ImpresionTask(className).execute(((ProductImage) holder).getM_url());
                        } else if(holder instanceof CpmImage){
                            new ImpresionTask(className).execute(((CpmImage) holder).getFullUrl());
                        }
                        holder.invoke();
                    }
                    revoke();
                }
            }
        });
    }

    private View getView() {
        return this;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private boolean isVisible(final View view) {
        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }
        Rect screen = new Rect(0, 0, getScreenWidth(), getOffsetHeight());

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int offset = getResources().getDimensionPixelOffset(com.tokopedia.topads.sdk.R.dimen.impressed_image_view_offset);
        float X = location[0] + offset;
        float Y = location[1] + offset;
        if (screen.top <= Y && screen.bottom >= Y && (screen.left) <= X && (screen.right) >= X) {
            return true;
        } else {
            return false;
        }
    }

    private int getOffsetHeight() {
        if (offset > 0) {
            return getScreenHeight() - offset;
        } else {
            return getScreenHeight() - getResources().getDimensionPixelOffset(com.tokopedia.topads.sdk.R.dimen.impressed_image_view_offset);
        }
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Use this for non topads
     *
     * @param holder
     * @param hintListener
     */
    public void setViewHintListener(ImpressHolder holder, ViewHintListener hintListener) {
        this.holder = holder;
        this.hintListener = hintListener;
    }

    @Deprecated
    /**
     * Use setViewHintListener(ImageHolder holder, ViewHintListener hintListener)
     */
    public void setViewHintListener(ViewHintListener hintListener) {
        this.hintListener = hintListener;
    }

    /**
     * Use this for topads product image
     *
     * @param image
     */
    public void setImage(ProductImage image) {
        this.holder = image;
        JvmMediaLoader.loadImage(this, image.getM_ecs());
    }

    /**
     * Use this for topads shop product image
     *
     * @param image
     */
    public void setImage(ImageProduct image) {
        this.holder = image;
        if (image.getImageUrl().isEmpty()) {
            setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color
                            .topads_gray_default_bg));
        } else {
            JvmMediaLoader.loadImage(this, image.getImageUrl());
        }
    }

    /**
     * Use this for topads headline shop
     *
     * @param image
     */
    public void setImage(CpmImage image) {
        this.holder = image;
        if (image.getFullEcs().isEmpty()) {
            setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color
                            .topads_gray_default_bg));
        } else {
            JvmMediaLoader.loadImage(this, image.getFullEcs());
        }
    }

    public interface ViewHintListener {
        void onViewHint();
    }
}
