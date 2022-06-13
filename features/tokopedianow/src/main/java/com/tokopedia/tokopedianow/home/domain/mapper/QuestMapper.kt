package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.common.util.JsonUtil.convertJsonStringToMap
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel

object QuestMapper {

    private const val BANNER_TITLE = "banner_title"
    private const val BANNER_DESCRIPTION = "banner_description"
    private const val ICON_URL = "banner_icon_url"

    fun mapQuestUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val uiModel = HomeQuestSequenceWidgetUiModel(
            id = response.id,
            state = HomeLayoutItemState.LOADING
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapQuestData(questListResponse: List<QuestList>): List<HomeQuestWidgetUiModel> {
        val questList = mutableListOf<HomeQuestWidgetUiModel>()
        questListResponse.forEach {
            val mapConfig = convertJsonStringToMap(it.config)
            questList.add(HomeQuestWidgetUiModel(
                id = it.id,
                title = getValueFromConfig(mapConfig, BANNER_TITLE),
                desc = getValueFromConfig(mapConfig, BANNER_DESCRIPTION),
                currentProgress = it.task.firstOrNull()?.progress?.current.orZero(),
                totalProgress = it.task.firstOrNull()?.progress?.target.orZero(),
                status = it.questUser.status,
                iconUrl = getValueFromConfig(mapConfig, ICON_URL)
            ))
        }
        return questList
    }

    private fun getValueFromConfig(config: Map<String, String>, key: String): String {
        return config[key].orEmpty()
    }
}