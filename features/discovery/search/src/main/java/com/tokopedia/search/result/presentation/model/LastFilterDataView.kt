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

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return 0
    }
}