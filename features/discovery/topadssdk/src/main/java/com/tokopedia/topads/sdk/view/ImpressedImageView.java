package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import android.view.ViewTreeObserver;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class ImpressedImageView extends AppCompatImageView {

    private static final String TAG = ImpressedImageView.class.getSimpleName();
    private ProductImage image;
    private ViewHintListener hintListener;
    private float radius = 8.0f;
    private Path path;
    private RectF rect;
    private int offset;

    public ImpressedImageView(Context context) {
        super(context);
        init();
    }

    public ImpressedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerObserver(this);
    }

    private void init() {
        path = new Path();
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

    private void registerObserver(View view){
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(isVisible(view) && image!=null && !image.isImpressed()){
                    if(hintListener!=null){
                        hintListener.onViewHint();
                    }
                    getViewTreeObserver().removeOnScrollChangedListener(this);
                    new ImpresionTask().execute(image.getM_url());
                    image.setImpressed(true);
                }
            }
        });
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

    public interface ViewHintListener {
        void onViewHint();
    }
}