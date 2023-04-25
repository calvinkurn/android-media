package com.tokopedia.topads.dashboard.recommendation.data.model.local

interface InsightListUiModel {
    fun id(): String
    fun equalsWith(newItem: InsightListUiModel): Boolean
}

data class InsightUiModel(
    val adGroups: MutableList<InsightListUiModel> = mutableListOf(),
    var nextCursor: String = "",
    var insightType: Int = 0
)

data class AdGroupUiModel(
    val adGroupID: String = "",
    val adGroupType: String = "",
    val count: Int = 0
) : InsightListUiModel {
    override fun id(): String {
        return adGroupID
    }

    override fun equalsWith(newItem: InsightListUiModel): Boolean {
        return this == newItem
    }

}

data class LoadingUiModel(val isLoading: Boolean = true) : InsightListUiModel {
    override fun id(): String {
        return ""
    }

    override fun equalsWith(newItem: InsightListUiModel): Boolean {
        return this == newItem
    }

}
