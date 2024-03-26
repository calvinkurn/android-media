package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory

class KeyWordItemUiModel(var keywordDataItem: KeywordDataItem) : ProductUiModel() {

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}
