package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by Rafli Syam on 13/09/21
 */

data class ShopProductSearchResultSuggestionUiModel(
    var suggestionText: String = "",
    var queryString: String = ""
) : BaseShopProductViewModel {

    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
