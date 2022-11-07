package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedplus.view.repository.FeedDetailRepository
import com.tokopedia.feedplus.view.subscriber.FeedDetailViewState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FeedDetailViewModel @Inject constructor(private var feedDetailRepository: FeedDetailRepository,
                                              private val userSession: UserSessionInterface) : ViewModel() {

    private var feedDetailLiveData: MutableLiveData<FeedDetailViewState> = MutableLiveData()
    private var pagingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var cursor: String = ""

    fun getPagingLiveData(): LiveData<Boolean> {
        return pagingLiveData
    }

    fun getFeedDetailLiveData(): LiveData<FeedDetailViewState> {
        return feedDetailLiveData
    }

    fun getFeedDetail(detailId: String, page: Int, shopId: String, activityId: String) {
        viewModelScope.launchCatchError(block = {
            if (page == 1) {
                feedDetailLiveData.value = FeedDetailViewState.LoadingState(isLoading = true, loadingMore = false)
            } else {
                feedDetailLiveData.value = FeedDetailViewState.LoadingState(isLoading = true, loadingMore = true)
            }
            val feedQuery = feedDetailRepository.fetchFeedDetail(detailId,  cursor)
            handleDataForFeedDetail(feedQuery, page, shopId, activityId)
        }, onError =
        {
            it.printStackTrace()
            feedDetailLiveData.value = FeedDetailViewState.Error(it)
        })
    }

    private fun handleDataForFeedDetail(feedQuery: FeedXGQLResponse,page: Int,  shopId: String, activityId: String) {
        cursor = feedQuery.data.nextCursor
        if (page == 1) {
            feedDetailLiveData.value = FeedDetailViewState.LoadingState(false, loadingMore = false)
            if (!hasFeed(feedQuery.data)) {
                feedDetailLiveData.value = FeedDetailViewState.SuccessWithNoData
                return
            }
        } else {
            feedDetailLiveData.value = FeedDetailViewState.LoadingState(false, loadingMore = true)
            if (!hasFeed(feedQuery.data)) {
                pagingLiveData.value = false
                return
            }
        }

        feedQuery.data.let {
            feedDetailLiveData.value = FeedDetailViewState.Success(it)
        }
    }

    private fun hasFeed(feedQuery: FeedXGetActivityProductsResponse): Boolean {
        return (feedQuery.products.isNotEmpty())
    }
}
