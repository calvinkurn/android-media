package com.tokopedia.search.result.shop.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory

internal data class ShopSuggestionDataView (
        val currentKeyword: String = "",
        val text: String = "",
        val query: String = "",
): Visitable<ShopListTypeFactory> {

    override fun type(typeFactory: ShopListTypeFactory?) = typeFactory?.type(this) ?: 0
}