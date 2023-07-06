package com.tokopedia.editshipping.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

/**
 * Created by Kris on 6/9/2016.
 * TOKOPEDIA
 */
public abstract class EditShippingCustomView<D, P, V, B> extends RelativeLayout{

    private B binding;
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
        binding = getLayoutView(context);
        bindView(binding);
    }

    protected B getBinding() {
        return binding;
    }

    protected abstract void bindView (B view);

    protected abstract B getLayoutView(Context context);

    public abstract void renderData(@NonNull D data);

    public abstract void setListener(P presenter);

    public abstract void setViewListener(V mainView);
}
