package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeQuestWidgetUiModel (
    val id: String = "",
    val title: String = "",
    val desc: String = "",
    val status: String = "",
    val currentProgress: Float = 0f,
    val totalProgress: Float = 0f,
    val iconUrl: String = "",
    val isErrorState: Boolean = false
) : HomeLayoutUiModel(id)  {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}