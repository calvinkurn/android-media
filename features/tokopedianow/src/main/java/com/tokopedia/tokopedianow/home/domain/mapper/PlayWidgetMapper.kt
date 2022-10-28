package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowSmallWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowMediumWidget
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel

object PlayWidgetMapper {

    fun mapToSmallPlayWidget(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val title = response.header.name
        val appLink = response.header.applink
        val widgetType = TokoNowSmallWidget(response.widgetParam)
        val playState = PlayWidgetState(isLoading = true)
        val model = playState.model.copy(title = title, actionAppLink = appLink)
        val widgetState = playState.copy(model = model)
        val layout = HomePlayWidgetUiModel(response.id, widgetType, widgetState)
        return HomeLayoutItemUiModel(layout, state)
    }

    fun mapToMediumPlayWidget(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val title = response.header.name
        val appLink = response.header.applink
        val widgetType = TokoNowMediumWidget(response.widgetParam)
        val playState = PlayWidgetState(isLoading = true)
        val model = playState.model.copy(title = title, actionAppLink = appLink)
        val widgetState = playState.copy(model = model)
        val layout = HomePlayWidgetUiModel(response.id, widgetType, widgetState)
        return HomeLayoutItemUiModel(layout, state)
    }
}