package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestWidgetList
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import java.util.regex.Matcher
import java.util.regex.Pattern

object QuestMapper {

    private const val REGEX_CONFIG = "\\s*\"(\\w+)\":\"(\\w+)\"\\s*"
    private const val FIRST_MAP_GROUP = 1
    private const val SECOND_MAP_GROUP = 2
    private const val BANNER_TITLE = "banner_title"

    fun mapQuestUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val uiModel = HomeQuestSequenceWidgetUiModel(
            id = response.id,
            state = HomeLayoutItemState.LOADING
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapQuestData(questListResponse: List<QuestWidgetList>): List<HomeQuestWidgetUiModel> {
        val questList = mutableListOf<HomeQuestWidgetUiModel>()
        questListResponse.forEach {
            questList.add(HomeQuestWidgetUiModel(
                id = it.id,
                title = getTitleFromConfig(it.config),
                desc = it.actionButton.shortText,
                currentProgress = it.task.firstOrNull()?.progress?.current.orZero(),
                totalProgress = it.task.firstOrNull()?.progress?.target.orZero(),
                status = it.questUser.status,
                appLink = it.actionButton.cta.appLink,
                iconUrl = it.widgetPrizeIconURL
            ))
        }
        return questList
    }

    private fun getTitleFromConfig(config: String): String {
        return mapConfig(config)[BANNER_TITLE].orEmpty()
    }

    private fun mapConfig(config: String): Map<String,String> {
        val pattern: Pattern = Pattern.compile(REGEX_CONFIG)
        val matcher: Matcher = pattern.matcher(config)

        val maps = mutableMapOf<String,String>()
        while (matcher.find()) {
            val key: String = matcher.group(FIRST_MAP_GROUP).orEmpty()
            val value: String = matcher.group(SECOND_MAP_GROUP).orEmpty()
            maps[key] = value
        }
        return maps
    }
}