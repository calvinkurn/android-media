package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeQuestTitleUiModel(
    val id: String = "",
    val currentQuestFinished: Int = 0,
    val totalQuestTarget: Int = 0,
    val isErrorState: Boolean = false
) : HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}