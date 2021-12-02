package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeQuestWidgetUiModel (
    val id: String,
    val currentProgress: Int,
    val totalProgress: Int,
    val title: String,
    val desc: String
) : HomeLayoutUiModel(id)  {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}