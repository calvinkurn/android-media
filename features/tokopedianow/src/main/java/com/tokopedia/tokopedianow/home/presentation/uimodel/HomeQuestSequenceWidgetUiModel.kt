package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeQuestSequenceWidgetUiModel (
    val id: String,
    val state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED,
    val questList: List<HomeQuestWidgetUiModel> = listOf()
) : HomeLayoutUiModel(id)  {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}