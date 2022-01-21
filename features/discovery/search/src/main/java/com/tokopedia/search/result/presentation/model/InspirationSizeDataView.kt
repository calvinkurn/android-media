package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class InspirationSizeDataView(
    val data: InspirationData = InspirationData(
        title = "",
        type = "",
        position = 0,
        optionCardData = listOf(),
        optionSizeData = listOf()
    )
) : Visitable<ProductListTypeFactory> {

    var activeOptions: List<String> = listOf()

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}