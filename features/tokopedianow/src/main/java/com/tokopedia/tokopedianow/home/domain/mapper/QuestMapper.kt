package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.common.util.JsonUtil.convertJsonStringToMap
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestShimmeringWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestWidgetUiModel

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

    fun mapQuestCardData(
        channelId: String,
        questListResponse: List<QuestList>
    ) = questListResponse.mapIndexed { index, it ->
        val mapConfig = convertJsonStringToMap(it.config)
        val currentProgress = it.task.firstOrNull()?.progress?.current.orZero()
        val totalProgress = it.task.firstOrNull()?.progress?.target.orZero()
        val previousQuest = questListResponse.getOrNull(index - 1)
        val questClaimed = previousQuest == null || previousQuest.isClaimed()
        val showLockedIcon = it.isIdle() && previousQuest?.isClaimed() == false
        val showStartBtn = it.isManualStart() && it.isIdle() && questClaimed

        HomeQuestCardItemUiModel(
            id = it.id,
            channelId = channelId,
            title = getValueFromConfig(mapConfig, BANNER_TITLE),
            description = getValueFromConfig(mapConfig, BANNER_DESCRIPTION),
            isLockedShown = showLockedIcon,
            showStartBtn = showStartBtn,
            isLoading = false,
            currentProgress = currentProgress,
            totalProgress = totalProgress,
            isIdle = it.isIdle()
        )
    }

    fun MutableList<HomeLayoutItemUiModel?>.updateQuestWidgetUiModel(
        channelId: String,
        questId: Int,
        isLoading: Boolean = false,
        showStartBtn: Boolean = true,
        isIdle: Boolean = true
    ) {
        updateQuestWidgetUiModel(channelId, questId) {
            it.copy(showStartBtn = showStartBtn, isLoading = isLoading, isIdle = isIdle)
        }
    }

    private fun MutableList<HomeLayoutItemUiModel?>.updateQuestWidgetUiModel(
        channelId: String,
        questId: Int,
        result: (HomeQuestCardItemUiModel) -> HomeQuestCardItemUiModel
    ) {
        find { it?.layout is HomeQuestWidgetUiModel && it.layout.id == channelId }?.let { itemUiModel ->
            val questWidget = itemUiModel.layout as HomeQuestWidgetUiModel
            val questList = questWidget.questList.toMutableList()
            val questItem = questList.first { it.id == questId.toString() }
            val questWidgetIndex = indexOf(itemUiModel)
            val questItemIndex = questList.indexOf(questItem)

            questList[questItemIndex] = result.invoke(questItem)

            val isStarted = !questList.all { it.isIdle }
            val newQuestWidget = questWidget.copy(questList = questList, isStarted = isStarted)
            this[questWidgetIndex] = itemUiModel.copy(layout = newQuestWidget)
        }
    }

    private fun getValueFromConfig(config: Map<String, String>, key: String): String {
        return config[key].orEmpty()
    }
}
