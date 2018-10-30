package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.EtalaseHighlightCarouselViewModel;
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel;

import java.util.ArrayList;
import java.util.List;

public class EtalaseHighlightAdapter extends BaseListAdapter<ShopProductEtalaseHighlightViewModel, EtalaseHighlightAdapterTypeFactory> {
    public EtalaseHighlightAdapter(EtalaseHighlightAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    @Override
    protected boolean isItemClickableByDefault() {
        return false;
    }

}
