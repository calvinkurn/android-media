package com.tokopedia.feedplus.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGQLResponse
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedplus.view.repository.FeedDetailRepository
import com.tokopedia.feedplus.view.subscriber.FeedDetailViewState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.net.HttpURLConnection
import javax.inject.Inject

class FeedDetailViewModel @Inject constructor(
    private val feedDetailRepository: FeedDetailRepository,
    private val mvcSummaryUseCase: MVCSummaryUseCase,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel() {

    private var feedDetailLiveData: MutableLiveData<FeedDetailViewState> = MutableLiveData()
    private var pagingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    var cursor: String = ""

    private val _merchantVoucherSummary =
        MutableLiveData<Result<TokopointsCatalogMVCSummary>>()
    val merchantVoucherSummary: LiveData<Result<TokopointsCatalogMVCSummary>>
        get() = _merchantVoucherSummary

    fun getPagingLiveData(): LiveData<Boolean> {
        return pagingLiveData
    }

    fun getFeedDetailLiveData(): LiveData<FeedDetailViewState> {
        return feedDetailLiveData
    }

    fun getFeedDetail(detailId: String, page: Int, shopId: String, activityId: String) {
        viewModelScope.launchCatchError(block = {
            if (page == 1) {
                feedDetailLiveData.value =
                    FeedDetailViewState.LoadingState(isLoading = true, loadingMore = false)
            } else {
                feedDetailLiveData.value =
                    FeedDetailViewState.LoadingState(isLoading = true, loadingMore = true)
            }
            val feedQuery = feedDetailRepository.fetchFeedDetail(detailId, cursor)
            handleDataForFeedDetail(feedQuery, page, shopId, activityId)
        }, onError =
        {
            it.printStackTrace()
            feedDetailLiveData.value = FeedDetailViewState.Error(it)
        })
    }

    fun fetchMerchantVoucherSummary(shopId: String) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))

            if (response.data?.resultStatus?.code != HttpURLConnection.HTTP_OK.toString()) {
                throw ResponseErrorException(response.data?.resultStatus?.message?.getOrNull(0))
            }

            response.data?.let {
                _merchantVoucherSummary.postValue(Success(it))
            }
        }) {
            _merchantVoucherSummary.postValue(Fail(it))
        }
    }

    private fun handleDataForFeedDetail(
        feedQuery: FeedXGQLResponse,
        page: Int,
        shopId: String,
        activityId: String
    ) {
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
