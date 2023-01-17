package com.tokopedia.quest_widget.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.quest_widget.constants.GQLQueryQuestWidget
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.data.WidgetData
import javax.inject.Inject

open class QuestWidgetUseCase @Inject constructor(var gqlWrapper: QuestGqlWrapper) {

    suspend fun getResponse(map: HashMap<String, Any>): WidgetData? {
        return gqlWrapper.getResponse(getResponseClass(), getQuery(), map)
    }

    fun getQueryParams(channel: Int, channelSlug: String, page: String): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[QuestWidgetListParams.CHANNEL] = channel
        variables[QuestWidgetListParams.CHANNEL_SLUG] = channelSlug
        variables[QuestWidgetListParams.PAGE] = page
        return variables
    }

    fun getResponseClass(): Class<WidgetData> {
        return WidgetData::class.java
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

object RetrieveQuestData{

    internal fun getQuestData(response: WidgetData?): QuestData {

        var questData = QuestData()
        response?.questWidgetList?.let { widgetData ->
            val configList = ArrayList<Config>()
            widgetData.questWidgetList?.forEach { questWidgetListItem ->
                configList.add(convertStringToConfig(questWidgetListItem.config))
            }
            questData = QuestData(configList, response)
            return questData
        }
        return questData
    }

    private fun convertStringToConfig(configString: String?) : Config {
        val dataClassType = object : TypeToken<Config>() {}.type
        return Gson().fromJson(configString, dataClassType)
    }

}