package com.tokopedia.review.feature.historydetails.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.feature.historydetails.data.ProductrevGetReviewDetail
import com.tokopedia.review.feature.historydetails.domain.ProductrevGetReviewDetailUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDetailViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
                                                userSession: UserSessionInterface,
                                                private val productrevGetReviewDetailUseCase: ProductrevGetReviewDetailUseCase)
    : BaseViewModel(coroutineDispatcherProvider.io()) {

    val userId = userSession.userId ?: ""

    private val _reviewDetails = MutableLiveData<ReviewViewState<ProductrevGetReviewDetail>>()
    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    private var feedbackId: Int = 0
    private var reputationId: Int = 0

    fun getReviewDetails() {
        _reviewDetails.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io()) {
                productrevGetReviewDetailUseCase.setRequestParams(feedbackId, reputationId)
                productrevGetReviewDetailUseCase.executeOnBackground()
            }
            _reviewDetails.postValue(Success(response.productrevGetReviewDetail))
        }) {
            _reviewDetails.postValue(Fail(it))
        }
    }

    fun setFeedbackAndReputationId(feedbackId: Int, reputationId: Int) {
        this.feedbackId = feedbackId
        this.reputationId = reputationId
    }
}