package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory

class KeyWordItemViewModel(var keywordDataItem: KeywordDataItem) : ProductViewModel() {

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}
