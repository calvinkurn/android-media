package com.tokopedia.tokopedianow.home.presentation.uimodel.quest

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

class HomeQuestWidgetUiModel(
    val id: String,
    val state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED,
    val questList: List<HomeQuestCardItemUiModel>
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}

