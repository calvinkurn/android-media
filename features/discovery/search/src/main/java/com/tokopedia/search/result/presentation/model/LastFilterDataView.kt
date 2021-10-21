package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class LastFilterDataView(
    val filterList: List<SavedOption> = listOf(),
    val title: String = "",
): Visitable<ProductListTypeFactory> {

    fun shouldShow() = filterList.isNotEmpty()

    fun optionNames(): String = filterList.joinToString { it.name }

    fun mapParameter() = filterList.associateBy(
        keySelector = { it.key },
        valueTransform = { it.value }
    )

    override fun type(typeFactory: ProductListTypeFactory?) =
        typeFactory?.type(this) ?: 0
}