package com.tokopedia.shop.sort.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.sort.view.adapter.viewholder.ShopProductSortViewHolder;
import com.tokopedia.shop.sort.view.model.ShopProductSortModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductSortAdapterTypeFactory extends BaseAdapterTypeFactory {

    public ShopProductSortAdapterTypeFactory() {
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        if (viewType == ShopProductSortViewHolder.LAYOUT) {
            return new ShopProductSortViewHolder(view);
        } else {
            return super.createViewHolder(view, viewType);
        }
    }

    public int type(ShopProductSortModel shopProductFilterModel) {
        return ShopProductSortViewHolder.LAYOUT;
    }
}
