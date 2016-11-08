package com.tokopedia.core.shipping.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public abstract class EditShippingCustomView<D, P, V> extends RelativeLayout{

    public EditShippingCustomView(Context context) {
        super(context);
        initView(context);
    }

    public EditShippingCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EditShippingCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutView();

    public abstract void renderData(@NonNull D data);

    public abstract void setListener(P presenter);

    public abstract void setViewListener(V mainView);
}
