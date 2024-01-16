package com.tokopedia.tokopedianow.home.presentation.uimodel.quest

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

data class HomeQuestReloadWidgetUiModel(
    val id: String,
    val mainTitle: String,
    val finishedWidgetTitle: String,
    val finishedWidgetContentDescription: String
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
