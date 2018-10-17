package com.tokopedia.digital.newcart.presentation.compoundview.adapter.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo;
import com.tokopedia.digital.R;

public class DigitalCartDetailTitleViewHolder extends RecyclerView.ViewHolder {
    public static final int LAYOUT = R.layout.item_digital_cart_detail_title;

    private AppCompatTextView titleTextView;

    public DigitalCartDetailTitleViewHolder(View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.tv_title);
    }

    public void bind(CartAdditionalInfo element) {
        titleTextView.setText(element.getTitle());
    }
}
