package com.tokopedia.quest_widget.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.quest_widget.data.WidgetData
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

const val ERROR_MSG = "Oops, ada sedikit gangguan. Coba daftar lagi, ya."
const val ERROR_NULL_RESPONSE = "Response is null"

class QuestWidgetViewModel @Inject constructor( workerDispatcher: CoroutineDispatcher, val questWidgetUseCase: QuestWidgetUseCase): BaseViewModel(workerDispatcher) {

    val questWidgetListLiveData = SingleLiveEvent<LiveDataResult<WidgetData>>()

    fun getWidgetList(channel: Int, channelSlug: String, page: String){
        launchCatchError(block = {
            questWidgetListLiveData.postValue(LiveDataResult.loading())
            val response = questWidgetUseCase.getResponse(questWidgetUseCase.getQueryParams(channel, channelSlug, page))
            if (response != null) {
                response.data?.let {
                    questWidgetListLiveData.postValue(LiveDataResult.success(it))
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
