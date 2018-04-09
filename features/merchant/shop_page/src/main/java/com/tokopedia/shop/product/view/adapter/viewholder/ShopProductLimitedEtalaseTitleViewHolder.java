package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductLimitedEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedSearchViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedEtalaseTitleViewHolder extends AbstractViewHolder<ShopProductLimitedEtalaseTitleViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_etalase_title_view;

    public ShopProductLimitedEtalaseTitleViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
    }

    @Override
    public void bind(ShopProductLimitedEtalaseTitleViewModel shopProductLimitedEtalaseTitleViewModel) {

    }
}