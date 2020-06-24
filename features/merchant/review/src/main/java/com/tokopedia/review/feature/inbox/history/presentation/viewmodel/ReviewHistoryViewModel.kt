package com.tokopedia.review.feature.inbox.history.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistoryResponse
import com.tokopedia.review.feature.inbox.history.domain.usecase.ProductrevFeedbackHistoryUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewHistoryViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatcherProvider,
        private val productrevFeedbackHistoryUseCase: ProductrevFeedbackHistoryUseCase
) : BaseViewModel(dispatchers.io()) {

    private val _reviewList = MutableLiveData<ReviewViewState<ProductrevFeedbackHistoryResponse>>()
    val reviewList: LiveData<ReviewViewState<ProductrevFeedbackHistoryResponse>>
        get() = _reviewList

    fun getReviewData(page: Int, searchQuery: String, isRefresh: Boolean = false) {
        if(isRefresh) {
            _reviewList.value = LoadingView
        }
        launchCatchError(block = {
            val response = withContext(dispatchers.io()) {
                productrevFeedbackHistoryUseCase.setParams(searchQuery, page)
                productrevFeedbackHistoryUseCase.executeOnBackground()
            }
            _reviewList.postValue(Success(response.productrevFeedbackHistoryResponse, page))
        }) {
            _reviewList.postValue(Fail(it, page))
        }
    }

}