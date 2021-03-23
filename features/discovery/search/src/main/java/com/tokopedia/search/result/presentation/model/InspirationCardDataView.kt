package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class InspirationCardDataView(
        val title: String = "",
        val type: String = "",
        val position: Int = 0,
        val optionData: List<InspirationCardOptionDataView> = listOf()
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isRelated() = type == SearchConstant.InspirationCard.TYPE_RELATED
}