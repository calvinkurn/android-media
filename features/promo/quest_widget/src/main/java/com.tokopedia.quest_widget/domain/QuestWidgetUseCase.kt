package com.tokopedia.quest_widget.domain

import com.tokopedia.quest_widget.constants.GQLQueryQuestWidget
import com.tokopedia.quest_widget.data.QuestWidgetResponse
import javax.inject.Inject

class QuestWidgetUseCase @Inject constructor(var gqlWrapper: QuestGqlWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): QuestWidgetResponse? {
        return gqlWrapper.getResponse(getResponseClass(), GQLQueryQuestWidget.QUERY_QUEST_WIDGET, map)
    }

    fun getQueryParams(channel: Int, channelSlug: String, page: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[QuestWidgetListParams.CHANNEL] = channel
        variables[QuestWidgetListParams.CHANNEL_SLUG] = channelSlug
        variables[QuestWidgetListParams.PAGE] = page
        return variables
    }

    fun getResponseClass(): Class<QuestWidgetResponse> {
        return QuestWidgetResponse::class.java
    }

    fun getQuery(): String {
        return GQLQueryQuestWidget.QUERY_QUEST_WIDGET
    }
}

object QuestWidgetListParams{
    const val CHANNEL = "channel"
    const val CHANNEL_SLUG = "channelSlug"
    const val PAGE = "page"
}