package com.tokopedia.talk.feature.inbox.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTracking
import com.tokopedia.talk.feature.inbox.data.DiscussionInbox
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import com.tokopedia.talk.feature.inbox.domain.usecase.TalkInboxListUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TalkInboxViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val talkInboxListUseCase: TalkInboxListUseCase,
        private val userSession: UserSessionInterface,
        private val talkInboxTracking: TalkInboxTracking
) : BaseViewModel(dispatcher.io) {

    private val _inboxList: MediatorLiveData<TalkInboxViewState<DiscussionInbox>> = MediatorLiveData()
    val inboxList: LiveData<TalkInboxViewState<DiscussionInbox>>
        get() = _inboxList

    private var shopId: String = ""
    private var unreadCount: Int = 0
    private var type: String = ""
    private var filter: TalkInboxFilter = TalkInboxFilter.TalkInboxNoFilter()
    private val page = MutableLiveData<Int>()

    init {
        _inboxList.addSource(page) {
            getInboxList(it)
        }
    }

    fun getShopId(): String {
        return shopId
    }

    fun getUnreadCount(): Int {
        return unreadCount
    }

    fun getActiveFilter(): String {
        return filter.filterParam
    }

    fun getType(): String {
        return type
    }

    fun getUserId(): String {
        return userSession.userId
    }


    fun setInboxType(inboxType: String) {
        this.type = inboxType
        resetPage()
    }

    fun setFilter(selectedFilter: TalkInboxFilter) {
        if(this.filter == selectedFilter) {
            talkInboxTracking.eventClickFilter(selectedFilter.filterParam, getType(), getUnreadCount(), false, getShopId(), getUserId())
            resetFilter()
            return
        }
        talkInboxTracking.eventClickFilter(selectedFilter.filterParam, getType(), getUnreadCount(), true, getShopId(), getUserId())
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
            _inboxList.postValue(TalkInboxViewState.Loading(page))
            talkInboxListUseCase.setRequestParam(type, filter.filterParam, page)
            val response = talkInboxListUseCase.executeOnBackground()
            shopId = response.discussionInbox.shopID
            unreadCount = if(type == TalkInboxTab.SHOP_TAB) {
                response.discussionInbox.sellerUnread
            } else {
                response.discussionInbox.buyerUnread
            }
            _inboxList.postValue(TalkInboxViewState.Success(response.discussionInbox, page, filter))
        }) {
            _inboxList.postValue(TalkInboxViewState.Fail(it, page))
        }
    }
}