package com.tokopedia.quest_widget.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.quest_widget.data.PageDetail
import com.tokopedia.quest_widget.data.QuestWidgetList
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher

const val ERROR_MSG = "Oops, ada sedikit gangguan. Coba daftar lagi, ya."
const val ERROR_NULL_RESPONSE = "Response is null"

class QuestWidgetViewModel( workerDispatcher: CoroutineDispatcher, val questWidgetUseCase: QuestWidgetUseCase): BaseViewModel(workerDispatcher) {

    val questWidgetListLiveData = SingleLiveEvent<com.tokopedia.quest_widget.util.LiveDataResult<QuestWidgetList>>()
    val pageDetailLiveData = SingleLiveEvent<com.tokopedia.quest_widget.util.LiveDataResult<PageDetail>>()
    val isEligibleLiveData = SingleLiveEvent<com.tokopedia.quest_widget.util.LiveDataResult<Boolean>>()

    fun getWidgetList(channel: Int, channelSlug: String, page: String){
        launchCatchError(block = {
            questWidgetListLiveData.postValue(com.tokopedia.quest_widget.util.LiveDataResult.loading())
            val response = questWidgetUseCase.getResponse(questWidgetUseCase.getQueryParams(channel, channelSlug, page))
            if (response != null) {
                response.data?.questWidgetList?.let {
                    questWidgetListLiveData.postValue(com.tokopedia.quest_widget.util.LiveDataResult.success(it))
                }
                response.data?.pageDetail?.let {
                    pageDetailLiveData.postValue(com.tokopedia.quest_widget.util.LiveDataResult.success(it))
                }
            }
            else
            {
                questWidgetListLiveData.postValue(com.tokopedia.quest_widget.util.LiveDataResult.error(Exception(ERROR_NULL_RESPONSE)))
            }
        }, onError = {
            questWidgetListLiveData.postValue(com.tokopedia.quest_widget.util.LiveDataResult.error(it))
        })
    }

}
