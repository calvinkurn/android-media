package com.tokopedia.topads.dashboard.recommendation.data.model.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface InsightListUiModel {
    fun id(): String
    fun equalsWith(newItem: InsightListUiModel): Boolean
}

data class InsightUiModel(
    val adGroups: MutableList<InsightListUiModel> = mutableListOf(),
    var nextCursor: String = "",
    var insightType: Int = 0
)

@Parcelize
data class AdGroupUiModel(
    val adGroupID: String = "",
    val adGroupName: String = "",
    val adGroupType: String = "",
    val count: Int = 0,
    val insightType: Int,
    val showGroupType: Boolean = false
) : InsightListUiModel, Parcelable {
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

data class EmptyStateUiListModel(
    val type: String = "",
    val statesList: List<EmptyStatesUiModel>
) : InsightListUiModel {
    override fun id(): String {
        return type
    }

    override fun equalsWith(newItem: InsightListUiModel): Boolean {
        return this == newItem
    }
}
