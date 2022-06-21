package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowSmallWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase.WidgetType.TokoNowMediumWidget
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
        val widget = TokoNowSmallWidget(response.widgetParam)
        val layout = HomePlayWidgetUiModel(response.id, title, widget)
        return HomeLayoutItemUiModel(layout, state)
    }

    fun mapToMediumPlayWidget(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val title = response.header.name
        val widget = TokoNowMediumWidget(response.widgetParam)
        val layout = HomePlayWidgetUiModel(response.id, title, widget)
        return HomeLayoutItemUiModel(layout, state)
    }
}