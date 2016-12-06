package com.tokopedia.core.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public abstract class EditShippingCourierView<D, L> extends LinearLayout{

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

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutView();

    public abstract void renderData(@NonNull D data, int index);

    public abstract void setViewListener(L mainViewListener);

}
