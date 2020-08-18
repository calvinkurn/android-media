package com.tokopedia.shop.search.view.adapter.model

import android.text.SpannableStringBuilder
import android.text.Spanned
import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory

class ShopSearchProductFixedResultDataModel(
        var searchQuery: String = "",
        var searchTypeLabel: Spanned = SpannableStringBuilder(""),
        override var type: Type
) : ShopSearchProductDataModel() {

    override fun type(
            typeFactory: ShopSearchProductAdapterTypeFactory
    ) = typeFactory.type(this)

}