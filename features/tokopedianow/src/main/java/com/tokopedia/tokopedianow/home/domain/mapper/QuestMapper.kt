package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import java.lang.Exception

object QuestMapper {

    private const val PREFIX_CURLY_BRACKET = "{"
    private const val SUFFIX_CURLY_BRACKET = "}"
    private const val DELIMITER_COMMA = ","
    private const val DELIMITER_COLON = ":"
    private const val DELIMITER_QUOTE = "\""
    private const val HTTP = "http"
    private const val DEFAULT_INDEX = 0
    private const val BANNER_TITLE = "banner_title"
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
            questList.add(HomeQuestWidgetUiModel(
                id = it.id,
                title = getValueFromConfig(it.config, BANNER_TITLE),
                desc = it.actionButton.shortText,
                currentProgress = it.task.firstOrNull()?.progress?.current.orZero(),
                totalProgress = it.task.firstOrNull()?.progress?.target.orZero(),
                status = it.questUser.status,
                appLink = it.actionButton.cta.appLink,
                iconUrl = getValueFromConfig(it.config, ICON_URL)
            ))
        }
        return questList
    }

    private fun getValueFromConfig(config: String, key: String): String {
        return mapConfig(config)[key].orEmpty()
    }

    private fun mapConfig(config: String): Map<String,String> {
        try {
            val list: List<String> = config.removeSurrounding(PREFIX_CURLY_BRACKET, SUFFIX_CURLY_BRACKET).split(DELIMITER_COMMA)
            val map = hashMapOf<String, String>()
            list.forEach {
                var key = ""
                var value = ""
                val keyValueList = it.split(DELIMITER_COLON)
                keyValueList.forEach { keyValue ->
                    if (keyValue != keyValueList[DEFAULT_INDEX]) {
                        if (keyValue.contains(HTTP)) {
                            value = "$keyValue:"
                        } else {
                            value += keyValue
                        }
                    } else {
                        key = keyValue.removeSurrounding(DELIMITER_QUOTE)
                    }
                }
                map[key] =  value.removeSurrounding(DELIMITER_QUOTE)
            }
            return map
        } catch (e: Exception) {
            return hashMapOf()
        }
    }
}