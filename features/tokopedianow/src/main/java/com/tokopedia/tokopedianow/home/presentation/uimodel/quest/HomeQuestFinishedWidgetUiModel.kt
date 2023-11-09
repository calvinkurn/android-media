package com.tokopedia.tokopedianow.home.presentation.uimodel.quest

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

data class HomeQuestFinishedWidgetUiModel(
    val id: String,
    val title: String,
    val contentDescription: String
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
