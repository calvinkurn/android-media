package com.tokopedia.mvcwidget.quest_widget

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher

const val ERROR_MSG = "Oops, ada sedikit gangguan. Coba daftar lagi, ya."
const val ERROR_NULL_RESPONSE = "Response is null"

class QuestWidgetViewModel( workerDispatcher: CoroutineDispatcher, val questWidgetUseCase: QuestWidgetUseCase): BaseViewModel(workerDispatcher) {

    val questWidgetListLiveData = SingleLiveEvent<LiveDataResult<QuestWidgetList>>()
    val pageDetailLiveData = SingleLiveEvent<LiveDataResult<PageDetail>>()
    val isEligibleLiveData = SingleLiveEvent<LiveDataResult<Boolean>>()

    fun getWidgetList(channel: Int, channelSlug: String, page: String){
        launchCatchError(block = {
            questWidgetListLiveData.postValue(LiveDataResult.loading())
            val response = questWidgetUseCase.getResponse(questWidgetUseCase.getQueryParams(channel, channelSlug, page))
            if (response != null) {
                response.data?.questWidgetList?.let {
                    questWidgetListLiveData.postValue(LiveDataResult.success(it))
                }
                response.data?.pageDetail?.let {
                    pageDetailLiveData.postValue(LiveDataResult.success(it))
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

}
