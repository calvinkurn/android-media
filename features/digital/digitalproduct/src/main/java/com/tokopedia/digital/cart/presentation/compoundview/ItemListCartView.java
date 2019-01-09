package com.tokopedia.digital.cart.presentation.compoundview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.digital.R;

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

public class ItemListCartView extends RelativeLayout {

    private TextView label;
    private TextView value;

    public ItemListCartView(Context context) {
        super(context);
        init(context);
    }

    public ItemListCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemListCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_list_checkout_digital_module, this, true);

        label = findViewById(R.id.label);
        value = findViewById(R.id.value);
    }

    public void bindView(CartItemDigital cartItemDigital) {
        label.setText(cartItemDigital.getLabel());
        value.setText(cartItemDigital.getValue());
        label.setVisibility(TextUtils.isEmpty(cartItemDigital.getLabel()) ? GONE : VISIBLE);
    }
}