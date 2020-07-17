package com.tokopedia.review.feature.historydetails.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
                                                private val userSession: UserSessionInterface,
                                                private val productrevGetReviewDetailUseCase: ProductrevGetReviewDetailUseCase)
    : BaseViewModel(coroutineDispatcherProvider.io()) {

    private val feedbackId = MutableLiveData<Int>()

    private val _reviewDetails = MediatorLiveData<ReviewViewState<ProductrevGetReviewDetail>>()

    val reviewDetails: LiveData<ReviewViewState<
            ProductrevGetReviewDetail>>
        get() = _reviewDetails

    init {
        _reviewDetails.addSource(feedbackId) {
            getReviewDetails(it)
        }
    }

    private fun getReviewDetails(feedbackId: Int) {
        _reviewDetails.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io()) {
                productrevGetReviewDetailUseCase.setRequestParams(feedbackId)
                productrevGetReviewDetailUseCase.executeOnBackground()
            }
            _reviewDetails.postValue(Success(response.productrevGetReviewDetail))
        }) {
            _reviewDetails.postValue(Fail(it))
        }
    }

    fun retry() {
        feedbackId.notifyObserver()
    }

    fun setFeedbackId(feedbackId: Int) {
        this.feedbackId.value = feedbackId
    }

    fun getUserId(): String {
        return userSession.userId
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}