package com.tokopedia.shop.newproduct.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.product.view.model.ShopProductEtalaseHighlightViewModel

class EtalaseHighlightAdapter(baseListAdapterTypeFactory: EtalaseHighlightAdapterTypeFactory) : BaseListAdapter<ShopProductEtalaseHighlightViewModel, EtalaseHighlightAdapterTypeFactory>(baseListAdapterTypeFactory) {

    override fun isItemClickableByDefault(): Boolean {
        return false
    }

}
