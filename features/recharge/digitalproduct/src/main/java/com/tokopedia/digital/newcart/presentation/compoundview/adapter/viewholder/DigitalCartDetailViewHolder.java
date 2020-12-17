package com.tokopedia.digital.newcart.presentation.compoundview.adapter.viewholder;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.presentation.model.cart.CartItemDigital;

public class DigitalCartDetailViewHolder extends RecyclerView.ViewHolder {
    public static final int LAYOUT = R.layout.view_digital_item_cart_detail;

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
