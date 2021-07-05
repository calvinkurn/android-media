package com.tokopedia.talk.feature.inbox.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
    private var unreadCount: Long = 0
    private var unrespondedCount: Long = 0
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

    fun getUnrespondedCount(): Long {
        return unrespondedCount
    }

    fun getUnreadCount(): Long {
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
        resetFilter()
    }

    fun setFilter(selectedFilter: TalkInboxFilter, isSellerView: Boolean, shouldTrack: Boolean) {
        if (this.filter == selectedFilter) {
            if(shouldTrack) {
                sendFilterTracker(selectedFilter, isSellerView, false)
            }
            resetFilter()
            return
        }
        if(shouldTrack) {
            sendFilterTracker(selectedFilter, isSellerView, true)
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

    private fun sendFilterTracker(selectedFilter: TalkInboxFilter, isSellerView: Boolean, isActive: Boolean) {
        if (isSellerView) {
            talkInboxTracking.eventClickSellerFilter(userSession.shopId, userSession.userId, selectedFilter.filterParam, isActive, unrespondedCount)
        } else {
            talkInboxTracking.eventClickFilter(selectedFilter.filterParam, getType(), unreadCount, false, getShopId(), getUserId())
        }
    }

    private fun resetFilter() {
        this.filter = TalkInboxFilter.TalkInboxNoFilter()
        resetPage()
    }

    private fun getInboxList(page: Int) {
        launchCatchError(block = {
            _inboxList.postValue(TalkInboxViewState.Loading(page))
            talkInboxListUseCase.setRequestParam(type, filter.filterParam, page)
            val response = talkInboxListUseCase.executeOnBackground()
            shopId = response.discussionInbox.shopID
            unreadCount = if (type == TalkInboxTab.SHOP_OLD) {
                response.discussionInbox.sellerUnread
            } else {
                response.discussionInbox.buyerUnread
            }
            unrespondedCount = response.discussionInbox.unrespondedTotal
            _inboxList.postValue(TalkInboxViewState.Success(response.discussionInbox, page, filter))
        }) {
            _inboxList.postValue(TalkInboxViewState.Fail(it, page))
        }
    }
}