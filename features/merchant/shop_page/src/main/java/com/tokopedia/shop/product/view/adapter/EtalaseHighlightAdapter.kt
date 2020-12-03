package com.tokopedia.shop.product.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseHighlightUiModel

class EtalaseHighlightAdapter(baseListAdapterTypeFactory: EtalaseHighlightAdapterTypeFactory) : BaseListAdapter<ShopProductEtalaseHighlightUiModel, EtalaseHighlightAdapterTypeFactory>(baseListAdapterTypeFactory) {

    override fun isItemClickableByDefault(): Boolean {
        return false
    }

}
