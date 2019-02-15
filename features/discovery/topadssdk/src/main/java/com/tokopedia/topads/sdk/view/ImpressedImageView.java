package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.CpmImage;
import com.tokopedia.topads.sdk.domain.model.ImageHolder;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import android.view.ViewTreeObserver;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class ImpressedImageView extends AppCompatImageView {

    private static final String TAG = ImpressedImageView.class.getSimpleName();
    private ImageHolder image;
    private ViewHintListener hintListener;
    private float radius = 0.0f;
    private Path path;
    private RectF rect;
    private int offset;
    private TypedArray styledAttributes;
    private ViewTreeObserver.OnScrollChangedListener scrollChangedListener;

    public ImpressedImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ImpressedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        invoke();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        revoke();
    }

    private void init(Context context, AttributeSet attrs) {
        path = new Path();
        styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ImpressedImageView, 0, 0);
        try{
            radius = styledAttributes.getDimension(R.styleable.ImpressedImageView_corner_radius, 8.0f);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
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

    private void revoke(){
        getViewTreeObserver().removeOnScrollChangedListener(getScrollChangedListener());
    }

    private void invoke(){
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                setScrollChangedListener(this);
                if (isVisible(getView())) {
                    if (image != null && !image.isInvoke()) {
                        if(hintListener!=null){
                            hintListener.onViewHint();
                        }
                        if(image instanceof ProductImage){
                            new ImpresionTask().execute(((ProductImage) image).getM_url());
                        } else if(image instanceof CpmImage){
                            new ImpresionTask().execute(((CpmImage) image).getFullUrl());
                        }
                        image.invoke();
                    }
                    if (getViewTreeObserver().isAlive()) {
                        getViewTreeObserver().removeOnScrollChangedListener(getScrollChangedListener());
                    }
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
        float X = location[0];
        float Y = location[1];
        if (screen.top <= Y && screen.bottom >= Y && screen.left <= X && screen.right >= X) {
            return true;
        } else {
            return false;
        }
    }

    private int getOffsetHeight() {
        if(offset > 0){
            return getScreenHeight() - offset;
        } else {
            return getScreenHeight() - getResources().getDimensionPixelOffset(R.dimen.dp_45);
        }
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void setViewHintListener(ViewHintListener hintListener) {
        this.hintListener = hintListener;
    }

    public void setImage(ProductImage image) {
        this.image = image;
        Glide.with(getContext()).load(image.getM_ecs()).into(this);
    }

    public void setImage(ImageProduct image) {
        this.image = image;
        if(image.getImageUrl().isEmpty()){
            setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color
                            .topads_gray_default_bg));
        } else {
            Glide.with(getContext()).load(image.getImageUrl()).into(this);
        }
    }

    public void setImage(CpmImage image) {
        this.image = image;
        if(image.getFullEcs().isEmpty()){
            setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color
                            .topads_gray_default_bg));
        } else {
            Glide.with(getContext()).load(image.getFullEcs()).into(this);
        }
    }

    public interface ViewHintListener {
        void onViewHint();
    }
}