package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomePlayWidgetUiModel(
    val id: String,
    val widgetType: WidgetType,
    val playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true),
    val isAutoRefresh: Boolean = false
): HomeLayoutUiModel(id) {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}