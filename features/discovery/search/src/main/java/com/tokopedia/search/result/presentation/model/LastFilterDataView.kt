package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class LastFilterDataView(
    val filterList: List<SavedOption> = listOf(),
    val title: String = "",
): Visitable<ProductListTypeFactory> {

    val impressHolder = ImpressHolder()

    fun shouldShow() = filterList.isNotEmpty()

    fun optionNames(): String = filterList.joinToString { it.name }

    fun filterOptions(): List<Option> = filterList
        .filter(SavedOption::isFilter)
        .map(SavedOption::asOption)

    fun sortOptions(): List<Option> = filterList
        .filter(SavedOption::isSort)
        .map(SavedOption::asOption)

    fun sortParameter(): Map<String, String> = sortOptions()
        .associateBy(
            keySelector = { it.key },
            valueTransform = { it.value }
        )

    override fun type(typeFactory: ProductListTypeFactory?) =
        typeFactory?.type(this) ?: 0
}