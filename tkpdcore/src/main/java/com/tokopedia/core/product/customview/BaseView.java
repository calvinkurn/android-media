package com.tokopedia.core.product.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * Created by Angga.Prasetiyo on 26/10/2015.
 */

/**
 * Abstrak class untuk customview product info
 *
 * @param <D> Data Model yang akan dirender
 * @param <L> Listener view dari main view (fragment atau activity)
 */
public abstract class BaseView<D, L> extends FrameLayout {
    private static final String TAG = BaseView.class.getSimpleName();

    protected L listener;

    public BaseView(Context context) {
        super(context);
        initView(context);
        parseAttribute(context, null);
        setViewListener();
    }


    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        parseAttribute(context, attrs);
        setViewListener();
    }

    public abstract void setListener(L listener);

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);
    }

    protected abstract int getLayoutView();

    protected abstract void parseAttribute(Context context, AttributeSet attrs);

    protected abstract void setViewListener();

    public abstract void renderData(@NonNull D data);

}
