package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.QuestListResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel

object QuestMapper {
    fun mapQuestUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val uiModel = HomeQuestSequenceWidgetUiModel(
            id = response.id,
            title = "Testing Aja Woy",
            seeAll = "Lihat nggak?",
            questList = emptyList(),
            state = HomeLayoutItemState.LOADING
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapQuestData(questListResponse: QuestListResponse): List<HomeQuestWidgetUiModel> {
        val questList = mutableListOf<HomeQuestWidgetUiModel>()
        questListResponse.questList.forEach {
            questList.add(HomeQuestWidgetUiModel(
                id = it.id,
                title = it.title,
                desc = it.description,
                currentProgress = it.task.firstOrNull()?.progress?.current.orZero(),
                totalProgress = it.task.firstOrNull()?.progress?.target.orZero(),
                status = it.questUser.status
            ))
        }
        return questList
    }
}