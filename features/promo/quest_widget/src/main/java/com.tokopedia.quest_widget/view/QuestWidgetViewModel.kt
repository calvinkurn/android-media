package com.tokopedia.quest_widget.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.quest_widget.constants.GQLQueryQuestWidget.IO
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.quest_widget.domain.RetrieveQuestData
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

const val ERROR_NULL_RESPONSE = "Response is null"
const val EMPTY_LOCATION = "page is empty"

class QuestWidgetViewModel @Inject constructor(@Named(IO) workerDispatcher: CoroutineDispatcher, val questWidgetUseCase: QuestWidgetUseCase): BaseViewModel(workerDispatcher) {

    val questWidgetListLiveData = SingleLiveEvent<LiveDataResult<QuestData>>()
    var page: String? = null

    fun getWidgetList(channel: Int, channelSlug: String, page: String, userSession: UserSession){

        if(userSession.isLoggedIn) {
            this.page = page
            if(page != "") {
                getQuestWidgetData(channel, channelSlug, page)
            }
            else {
                questWidgetListLiveData.postValue(
                    LiveDataResult.error(
                        Exception(
                            EMPTY_LOCATION
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
            if (response != null){
                if(response.questWidgetList?.resultStatus?.code == "200") {
                    if (response.questWidgetList.isEligible == false) {
                        questWidgetListLiveData.postValue(LiveDataResult.emptyData())
                    }
                    else {
                        questWidgetListLiveData.postValue(
                            LiveDataResult.success(
                                RetrieveQuestData.getQuestData(
                                    response
                                )
                            )
                        )
                    }
                }
                else{
                    questWidgetListLiveData.postValue(
                        LiveDataResult.error(
                            Exception(
                                ERROR_NULL_RESPONSE
                            )
                        )
                    )
                }
            }
            else {
                questWidgetListLiveData.postValue(LiveDataResult.emptyData())
            }
        }, onError = {
            questWidgetListLiveData.postValue(LiveDataResult.error(it))
        })
    }
}
