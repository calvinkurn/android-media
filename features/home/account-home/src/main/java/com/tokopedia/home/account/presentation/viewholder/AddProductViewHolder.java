package com.tokopedia.home.account.presentation.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.viewmodel.AddProductViewModel;

/**
 * @author okasurya on 7/27/18.
 */
public class AddProductViewHolder extends AbstractViewHolder<AddProductViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_add_product;

    private Button buttonAddProduct;

    public AddProductViewHolder(View view, AccountItemListener listener) {
        super(view);
        buttonAddProduct = itemView.findViewById(R.id.button_add_product);
        buttonAddProduct.setOnClickListener(v -> listener.onAddProductClicked());
    }

    @Override
    public void bind(AddProductViewModel element) {
    }
}
