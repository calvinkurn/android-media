package com.tokopedia.editshipping.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public abstract class EditShippingCourierView<D, L, V> extends LinearLayout{

    private V binding;

    public EditShippingCourierView(Context context) {
        super(context);
        initView(context);
    }

    public EditShippingCourierView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView(context);
    }

    public EditShippingCourierView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initView(context);
    }

    protected V getBinding() {
        return binding;
    }

    protected void initView(Context context) {
       binding = getLayoutView(context);
    }

    protected abstract V getLayoutView(Context context);

    public abstract void renderData(@NonNull D data, int index);

    public abstract void setViewListener(L mainViewListener);

}
