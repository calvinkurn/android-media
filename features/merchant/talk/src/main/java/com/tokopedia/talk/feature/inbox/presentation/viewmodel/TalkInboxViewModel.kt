package com.tokopedia.talk.feature.inbox.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.domain.usecase.TalkInboxListUseCase
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import javax.inject.Inject

class TalkInboxViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val talkInboxListUseCase: TalkInboxListUseCase
) : BaseViewModel(dispatcher.io) {

    private val _inboxList: MediatorLiveData<TalkInboxViewState<List<TalkInboxUiModel>>> = MediatorLiveData()
    val inboxList: LiveData<TalkInboxViewState<List<TalkInboxUiModel>>>
        get() = _inboxList

    private var shopId: String = ""
    private var type: String = ""
    private var filter: TalkInboxFilter = TalkInboxFilter.TalkInboxNoFilter()

    fun getShopId(): String {
        return shopId
    }

    private val page = MutableLiveData<Int>()

    init {
        _inboxList.addSource(page) {
            getInboxList(it)
        }
    }

    fun setInboxType(inboxType: String) {
        this.type = inboxType
        resetPage()
    }

    fun setFilter(selectedFilter: TalkInboxFilter) {
        if(this.filter == selectedFilter) {
            resetFilter()
            return
        }
        this.filter = selectedFilter
        resetPage()
    }

    fun updatePage(page: Int) {
        this.page.value = page
    }

    fun resetPage() {
        this.page.value = TalkConstants.DEFAULT_INITIAL_PAGE
    }

    private fun resetFilter() {
        this.filter = TalkInboxFilter.TalkInboxNoFilter()
        resetPage()
    }

    private fun getInboxList(page: Int = 0) {
        launchCatchError(block = {
            talkInboxListUseCase.setRequestParam(type, filter.filterParam, page)
            val response = talkInboxListUseCase.executeOnBackground()
            with(response.discussionInbox) {
                shopId = shopID
                _inboxList.postValue(TalkInboxViewState.Success(inbox.map { TalkInboxUiModel(it) }, page, filter, hasNext))
            }
        }) {
            _inboxList.postValue(TalkInboxViewState.Fail(it))
        }
    }
}