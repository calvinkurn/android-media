package com.tokopedia.digital.newcart.presentation.compoundview.adapter.viewholder;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.presentation.model.cart.CartAdditionalInfo;

public class DigitalCartDetailTitleViewHolder extends RecyclerView.ViewHolder {
    public static final int LAYOUT = R.layout.view_digital_title_item_cart_detail;

    private AppCompatTextView titleTextView;

    public DigitalCartDetailTitleViewHolder(View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.tv_title);
    }

    public void bind(CartAdditionalInfo element) {
        titleTextView.setText(element.getTitle());
    }
}
