package com.tokopedia.topads.dashboard.recommendation.data.model.local

interface GroupListUiModel {
    fun id(): String
    fun equalsWith(newItem: GroupListUiModel): Boolean
}

data class GroupItemUiModel(
    val groupName: String = "",
    val groupId: String,
    var isSelected: Boolean,
    val keywordCount: Int,
    val productCount: Int,
) : GroupListUiModel {
    override fun id(): String {
        return groupId
    }

    override fun equalsWith(newItem: GroupListUiModel): Boolean {
        return this == newItem
    }
}

data class EmptyGroupListUiModel(
    val groupId: String = ""
) : GroupListUiModel {
    override fun id(): String {
        return groupId
    }

    override fun equalsWith(newItem: GroupListUiModel): Boolean {
        return this == newItem
    }
}

data class FailedGroupListUiModel(
    val groupId: String = ""
) : GroupListUiModel {
    override fun id(): String {
        return groupId
    }

    override fun equalsWith(newItem: GroupListUiModel): Boolean {
        return this == newItem
    }
}

data class LoadingGroupsUiModel(
    val id: String = ""
) : GroupListUiModel {
    override fun id(): String {
        return id
    }

    override fun equalsWith(newItem: GroupListUiModel): Boolean {
        return this == newItem
    }
}
