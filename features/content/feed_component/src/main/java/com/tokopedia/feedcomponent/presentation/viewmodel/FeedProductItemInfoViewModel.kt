package com.tokopedia.feedcomponent.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXGetActivityProductsResponse
import com.tokopedia.feedcomponent.domain.usecase.FeedXGetActivityProductsUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedXProductResult
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * ViewModel for Product Item Info BottomSheet
 *
 * IMO
 * This "presentation" package later on will be used instead of "view"
 * because the "view" package have many tech debts in structure and naming
 *
 */
class FeedProductItemInfoViewModel @Inject constructor(
    private val mvcSummaryUseCase: MVCSummaryUseCase,
    private val feedXGetActivityProductsUseCase: FeedXGetActivityProductsUseCase,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    var cursor: String = ""
    private var lastPagingLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getPagingLiveData(): LiveData<Int> {
        return lastPagingLiveData
    }

    private val _merchantVoucherSummary =
        MutableLiveData<Result<TokopointsCatalogMVCSummary>>()
    val merchantVoucherSummary: LiveData<Result<TokopointsCatalogMVCSummary>>
        get() = _merchantVoucherSummary

    private val _feedProductsResponse =
        MutableLiveData<FeedXProductResult>()
    val feedProductsResponse: LiveData<FeedXProductResult>
        get() = _feedProductsResponse

    fun fetchMerchantVoucherSummary(shopId: String) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            val resultStatus = response.data?.resultStatus

            if (resultStatus?.code != "200") {
                throw ResponseErrorException(resultStatus?.message?.get(0).toString())
            }

            response.data?.let {
                _merchantVoucherSummary.postValue(Success(it))
            }
        }) {
            _merchantVoucherSummary.postValue(Fail(it))
        }
    }

    fun fetchFeedXProductsData(activityId: String = "224467262", page: Int = 1) {
        launchCatchError(dispatcherProvider.main, block = {
            if (page == 1) {
                _feedProductsResponse.value =
                    FeedXProductResult.LoadingState(isLoading = true, loadingMore = false)
            } else {
                _feedProductsResponse.value =
                    FeedXProductResult.LoadingState(isLoading = true, loadingMore = true)
            }
            feedXGetActivityProductsUseCase.setRequestParams(
                feedXGetActivityProductsUseCase.getFeedDetailParam(
                    activityId, cursor
                )
            )
            val response = feedXGetActivityProductsUseCase.executeOnBackground()
            handleDataForFeedDetail(response.data, page)
        }, onError =
        {
            it.printStackTrace()
            _feedProductsResponse.value = FeedXProductResult.Error(it)
        })
    }

    private fun handleDataForFeedDetail(
        feedXGetActivityProductsResponse: FeedXGetActivityProductsResponse,
        page: Int
    ) {
        cursor = feedXGetActivityProductsResponse.nextCursor
        if (page == 1) {
            _feedProductsResponse.value =
                FeedXProductResult.LoadingState(false, loadingMore = false)
            if (!hasFeed(feedXGetActivityProductsResponse)) {
                _feedProductsResponse.value = FeedXProductResult.SuccessWithNoData
                return
            }
        } else {
            _feedProductsResponse.value = FeedXProductResult.LoadingState(false, loadingMore = true)
            if (!hasFeed(feedXGetActivityProductsResponse)) {
                return
            }
        }
        lastPagingLiveData.value = page + 1

        feedXGetActivityProductsResponse.let {
            _feedProductsResponse.value = FeedXProductResult.Success(it)
        }
    }

    private fun hasFeed(feedXGetActivityProductsResponse: FeedXGetActivityProductsResponse): Boolean {
        return (feedXGetActivityProductsResponse.products.isNotEmpty())
    }
}
