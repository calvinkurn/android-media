package com.tokopedia.quest_widget.data

data class QuestWidgetDataModel(
    var questItemList: List<QuestWidgetListItem> = listOf(),
    val title: String = "",
    val subtitle: String = "",
)