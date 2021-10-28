package com.tokopedia.quest_widget.view

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.constants.GQLQueryQuestWidget.IO
import com.tokopedia.quest_widget.data.WidgetData
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

const val ERROR_MSG = "Oops, ada sedikit gangguan. Coba daftar lagi, ya."
const val ERROR_NULL_RESPONSE = "Response is null"

class QuestWidgetViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher, val questWidgetUseCase: QuestWidgetUseCase): BaseViewModel(workerDispatcher) {

    private val TAG = QuestWidgetViewModel::class.java.simpleName
    val questWidgetListLiveData = SingleLiveEvent<LiveDataResult<QuestData>>()

    fun getWidgetList(channel: Int, channelSlug: String, page: String){
        launchCatchError(block = {
            questWidgetListLiveData.postValue(LiveDataResult.loading())
            val response = questWidgetUseCase.getResponse(questWidgetUseCase.getQueryParams(channel, channelSlug, page))
            Log.d(TAG, "getWidgetList: " + response?.questWidgetList)
            if (response != null) {
                response.questWidgetList?.let { widgetData ->
                    val configList = ArrayList<Config>()
                    widgetData.questWidgetList.forEach { questWidgetListItem ->
                        configList.add(convertStringToConfig(questWidgetListItem.config))
                    }
                    val questData = QuestData(configList, response)
                    questWidgetListLiveData.postValue(LiveDataResult.success(questData))
                }
            }
            else
            {
                questWidgetListLiveData.postValue(LiveDataResult.error(Exception(ERROR_NULL_RESPONSE)))
            }
        }, onError = {
            questWidgetListLiveData.postValue(LiveDataResult.error(it))
        })
    }

    private fun convertStringToConfig(configString: String?) : Config {
        val dataClassType = object : TypeToken<Config>() {}.type
        return Gson().fromJson(configString, dataClassType)
    }

}
