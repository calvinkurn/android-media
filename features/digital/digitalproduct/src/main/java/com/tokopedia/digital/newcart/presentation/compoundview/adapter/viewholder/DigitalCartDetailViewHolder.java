package com.tokopedia.digital.newcart.presentation.compoundview.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital;
import com.tokopedia.digital.R;

public class DigitalCartDetailViewHolder extends RecyclerView.ViewHolder {
    public static final int LAYOUT = R.layout.item_digital_cart_detail;

    private AppCompatTextView labelTextView;
    private AppCompatTextView valueTextView;

    public DigitalCartDetailViewHolder(View itemView) {
        super(itemView);
        labelTextView = itemView.findViewById(R.id.tv_label);
        valueTextView = itemView.findViewById(R.id.tv_value);
    }

    public void bind(CartItemDigital element) {
        labelTextView.setText(element.getLabel());
        valueTextView.setText(element.getValue());
    }
}
