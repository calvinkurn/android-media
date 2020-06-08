package com.tokopedia.review.feature.inbox.pending.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.coroutine.CoroutineDispatchers
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedback
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponse
import com.tokopedia.review.feature.inbox.pending.data.ReviewPendingViewState
import com.tokopedia.review.feature.inbox.pending.domain.usecase.ProductrevWaitForFeedbackUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewPendingViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val productrevWaitForFeedbackUseCase: ProductrevWaitForFeedbackUseCase
) : BaseViewModel(dispatchers.io) {

    private val _reviewList = MutableLiveData<Result<ProductrevWaitForFeedbackResponse>>()
    val reviewList: LiveData<Result<ProductrevWaitForFeedbackResponse>>
        get() = _reviewList

    val reviewViewState : LiveData<ReviewPendingViewState> = Transformations.map(reviewList) {
        updateUI(it)
    }

    fun getReviewData(page: Int) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                productrevWaitForFeedbackUseCase.setParams(page = page)
                productrevWaitForFeedbackUseCase.executeOnBackground()
            }
            _reviewList.postValue(Success(response.productrevWaitForFeedbackWaitForFeedback))
        }) {
            _reviewList.postValue(Fail(Throwable(page.toString())))
        }
    }

    private fun updateUI(gqlResponse: Result<ProductrevWaitForFeedbackResponse>) : ReviewPendingViewState {
        return when(gqlResponse) {
            is Success -> {
                with(gqlResponse.data) {
                    ReviewPendingViewState.ReviewPendingSuccess(list.isEmpty(), page)
                }
            }
            is Fail -> {
                gqlResponse.throwable.message?.let {
                    if(it != ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE.toString()) {
                        ReviewPendingViewState.ReviewPendingLazyLoadError
                    }
                }
                ReviewPendingViewState.ReviewPendingInitialLoadError
            }
        }
    }


}