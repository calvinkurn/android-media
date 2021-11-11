package com.tokopedia.quest_widget.view

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.quest_widget.constants.GQLQueryQuestWidget.IO
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

const val ERROR_MSG = "Oops, ada sedikit gangguan. Coba daftar lagi, ya."
const val ERROR_NULL_RESPONSE = "Response is null"
const val LOCATION_NOT_ALLOWED = "Location not allowed"

class QuestWidgetViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher, val questWidgetUseCase: QuestWidgetUseCase): BaseViewModel(workerDispatcher) {

    val questWidgetListLiveData = SingleLiveEvent<LiveDataResult<QuestData>>()
    lateinit var page: String

    fun getWidgetList(channel: Int, channelSlug: String, page: String, userSession: UserSession){

        if(userSession.isLoggedIn) {
            this.page = page
            if(page != "" && (page == QuestWidgetLocations.HOME_PAGE || page == QuestWidgetLocations.MY_REWARD ||page == QuestWidgetLocations.DISCO)) {
                getQuestWidgetData(channel, channelSlug, page)
            }
            else {
                questWidgetListLiveData.postValue(
                    LiveDataResult.error(
                        Exception(
                            LOCATION_NOT_ALLOWED
                        )
                    )
                )
            }
        }
        else{
            questWidgetListLiveData.postValue(LiveDataResult.nonLogin())
        }
    }

    fun getQuestWidgetData(channel: Int, channelSlug: String, page: String) {

        launchCatchError(block = {
            questWidgetListLiveData.postValue(LiveDataResult.loading())
            val response = questWidgetUseCase.getResponse(
                questWidgetUseCase.getQueryParams(
                    channel,
                    channelSlug,
                    page
                )
            )
            if (response != null) {
                response.questWidgetList.let { widgetData ->
                    val configList = ArrayList<Config>()
                    widgetData.questWidgetList.forEach { questWidgetListItem ->
                        configList.add(convertStringToConfig(questWidgetListItem.config))
                    }
                    val questData = QuestData(configList, response)
                    questWidgetListLiveData.postValue(LiveDataResult.success(questData))
                }
            }
            else {
                questWidgetListLiveData.postValue(
                    LiveDataResult.error(
                        Exception(
                            ERROR_NULL_RESPONSE
                        )
                    )
                )
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
