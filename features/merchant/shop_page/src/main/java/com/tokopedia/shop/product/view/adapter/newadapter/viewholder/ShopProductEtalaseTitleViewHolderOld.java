package com.tokopedia.shop.product.view.adapter.newadapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductEtalaseTitleViewModelOld;

/**
 * @author by alvarisi on 12/12/17.
 */
@Deprecated
public class ShopProductEtalaseTitleViewHolderOld extends AbstractViewHolder<ShopProductEtalaseTitleViewModelOld> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_etalase_title_view_old;

    public ShopProductEtalaseTitleViewHolderOld(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
    }

    @Override
    public void bind(ShopProductEtalaseTitleViewModelOld shopProductLimitedEtalaseTitleViewModel) {

    }
}