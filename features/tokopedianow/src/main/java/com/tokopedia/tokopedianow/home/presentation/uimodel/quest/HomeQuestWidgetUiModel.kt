package com.tokopedia.tokopedianow.home.presentation.uimodel.quest

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

data class HomeQuestWidgetUiModel(
    val id: String,
    val title: String,
    val questList: List<HomeQuestCardItemUiModel>,
    val currentProgressPosition: Int
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}

