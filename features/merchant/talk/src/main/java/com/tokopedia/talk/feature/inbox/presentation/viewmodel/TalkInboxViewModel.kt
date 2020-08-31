package com.tokopedia.talk.feature.inbox.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.inbox.domain.usecase.TalkInboxListUseCase
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxViewState
import javax.inject.Inject

class TalkInboxViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val talkInboxListUseCase: TalkInboxListUseCase
) : BaseViewModel(dispatcher.io) {

    private val _inboxList: MutableLiveData<TalkInboxViewState<List<TalkInboxUiModel>>> = MutableLiveData()
    val inboxList: LiveData<TalkInboxViewState<List<TalkInboxUiModel>>>
        get() = _inboxList

    private var shopId: String = ""

    fun getShopId(): String {
        return shopId
    }

    fun getInboxList(type: String, filter: String, page: Int = 0) {
        launchCatchError(block = {
            talkInboxListUseCase.setRequestParam(type, filter, page)
            val response = talkInboxListUseCase.executeOnBackground()
            with(response.discussionInbox) {
                shopId = shopID
                _inboxList.postValue(TalkInboxViewState.Success(inbox.map { TalkInboxUiModel(it) }))
            }
        }) {
            _inboxList.postValue(TalkInboxViewState.Fail(it))
        }
    }
}