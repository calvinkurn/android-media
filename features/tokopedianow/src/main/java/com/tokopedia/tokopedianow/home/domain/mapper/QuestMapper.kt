package com.tokopedia.tokopedianow.home.domain.mapper

import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestList
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import java.lang.Exception

object QuestMapper {

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
        var map: Map<String, String> = HashMap()
        map = try {
            Gson().fromJson(config, map.javaClass)
        } catch (e: Exception) {
            mapOf()
        }
        return map[key].orEmpty()
    }
}