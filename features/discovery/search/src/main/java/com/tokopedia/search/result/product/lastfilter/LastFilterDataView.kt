package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class LastFilterDataView(
    val filterList: List<SavedOption> = listOf(),
    val title: String = "",
    val applink: String = "",
    val keyword: String = "",
    val filterParamsString: String = "",
    val trackingOption: Int = 0,
    val componentId: String = "",
    val dimension90: String = "",
) : Visitable<ProductListTypeFactory>, ImpressHolder(),
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        applink = applink,
        valueName = filterParamsString,
        componentId = componentId,
        dimension90 = dimension90,
    ) {

    fun shouldShow() = filterList.isNotEmpty()

    fun optionNames(): String = filterList.joinToString { it.name }

    fun filterOptions(): List<Option> =
        filterList
            .filter(SavedOption::isFilter)
            .map(SavedOption::asOption)

    fun sortOptions(): List<Option> =
        filterList
            .filter(SavedOption::isSort)
            .map(SavedOption::asOption)

    fun sortParameter(): Map<String, String> =
        sortOptions()
            .associateBy(
                keySelector = { it.key },
                valueTransform = { it.value }
            )

    fun sortFilterParamsString(): String {
        val optionList = filterList.map(SavedOption::asOption)
        val optionMap = OptionHelper.toMap(optionList) as Map<String?, String>
        return getSortFilterParamsString(optionMap)
    }

    override fun type(typeFactory: ProductListTypeFactory?) =
        typeFactory?.type(this) ?: 0
}