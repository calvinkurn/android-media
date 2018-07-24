package com.tokopedia.shop.product.view.adapter.viewholder;

import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductMoreViewModel;

/**
 * Created by zulfikarrahman on 6/4/18.
 */

public class ShopProductMoreViewHolder extends AbstractViewHolder<ShopProductMoreViewModel> {
    public static final int LAYOUT = R.layout.item_shop_product_view_more;
    private final View.OnClickListener onClickViewMoreListener;
    private Button buttonViewMore;

    public ShopProductMoreViewHolder(View parent, View.OnClickListener onClickViewMoreListener) {
        super(parent);
        this.onClickViewMoreListener = onClickViewMoreListener;
        buttonViewMore = parent.findViewById(R.id.button_view_more);
    }

    @Override
    public void bind(ShopProductMoreViewModel element) {
        buttonViewMore.setOnClickListener(onClickViewMoreListener);
    }
}
