package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class HideViewHolder extends AbstractViewHolder<BaseShopProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_hide;

    public HideViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
    }

    @Override
    public void bind(BaseShopProductViewModel baseShopProductViewModel) {

    }
}