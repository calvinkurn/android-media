package com.tokopedia.review.feature.inbox.pending.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.coroutine.CoroutineDispatchers
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponse
import com.tokopedia.review.feature.inbox.pending.domain.usecase.ProductrevWaitForFeedbackUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewPendingViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val productrevWaitForFeedbackUseCase: ProductrevWaitForFeedbackUseCase
) : BaseViewModel(dispatchers.io) {

    private val _reviewList = MutableLiveData<ReviewViewState<ProductrevWaitForFeedbackResponse>>()
    val reviewList: LiveData<ReviewViewState<ProductrevWaitForFeedbackResponse>>
        get() = _reviewList

    fun getReviewData(page: Int, isRefresh: Boolean = false) {
        if(isRefresh) {
            _reviewList.value = LoadingView
        }
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                productrevWaitForFeedbackUseCase.setParams(page = page)
                productrevWaitForFeedbackUseCase.executeOnBackground()
            }
            _reviewList.postValue(Success(response.productrevWaitForFeedbackWaitForFeedback, page))
        }) {
            _reviewList.postValue(Fail(it, page))
        }
    }

}