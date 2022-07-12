package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.CATEGORY_ID_L2
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper

class LastFilterModel(
    @SerializedName("fetchLastFilter")
    val lastFilter: LastFilter = LastFilter(),
) {

    class LastFilter(
        @SerializedName("data")
        val data: Data = Data(),
    )

    class Data(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName(CATEGORY_ID_L2)
        val categoryIdL2: String = "",

        @SerializedName("applink")
        val applink: String = "",

        @SerializedName("filters")
        val filters: List<SavedOption> = listOf(),

        @SerializedName("tracking_option")
        val trackingOption: Int = 0,

        @SerializedName("component_id")
        val componentId: String ="",
    ) {

        fun sortFilterParamsString(): String {
            val optionList = filters.map(SavedOption::asOption)
            val optionMap = OptionHelper.toMap(optionList) as Map<String?, String>
            return getSortFilterParamsString(optionMap)
        }
    }
}