package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeQuestWidgetUiModel (
    val id: String,
    val title: String,
    val desc: String,
    val status: String,
    val currentProgress: Float,
    val totalProgress: Float,
) : HomeLayoutUiModel(id)  {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}