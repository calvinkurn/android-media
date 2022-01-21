package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class InspirationCardDataView(
    val data: InspirationData = InspirationData()
) : Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isRelated() = data.type == SearchConstant.InspirationCard.TYPE_RELATED
}