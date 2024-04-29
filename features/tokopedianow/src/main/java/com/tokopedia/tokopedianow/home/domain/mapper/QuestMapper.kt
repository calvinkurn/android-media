package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.common.util.JsonUtil.convertJsonStringToMap
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestShimmeringWidgetUiModel

object QuestMapper {
    private const val BANNER_TITLE = "banner_title"
    private const val BANNER_DESCRIPTION = "banner_description"

    fun mapQuestWidgetUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val uiModel = HomeQuestShimmeringWidgetUiModel(
            id = response.id,
            mainTitle = response.header.name,
            finishedWidgetTitle = response.header.subtitle,
            finishedWidgetContentDescription = response.widgetParam
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapQuestCardData(questListResponse: List<QuestList>): List<HomeQuestCardItemUiModel> = questListResponse.map {
        val mapConfig = convertJsonStringToMap(it.config)
        val currentProgress = it.task.firstOrNull()?.progress?.current.orZero()
        val totalProgress = it.task.firstOrNull()?.progress?.target.orZero()
        HomeQuestCardItemUiModel(
            id = it.id,
            title = getValueFromConfig(mapConfig, BANNER_TITLE),
            description = getValueFromConfig(mapConfig, BANNER_DESCRIPTION),
            isLockedShown = it.isIdle(),
            currentProgress = currentProgress,
            totalProgress = totalProgress
        )
    }

    private fun getValueFromConfig(config: Map<String, String>, key: String): String {
        return config[key].orEmpty()
    }
}
