package com.tokopedia.review.feature.historydetails.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDetailViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
                                                private val userSession: UserSessionInterface,
                                                private val productrevGetReviewDetailUseCase: ProductrevGetReviewDetailUseCase)
    : BaseViewModel(coroutineDispatcherProvider.io()) {

    private val _feedbackId = MutableLiveData<Int>()

    private val _reviewDetails = MediatorLiveData<ReviewViewState<ProductrevGetReviewDetail>>()

    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    val feedbackId: Int
        get() = _feedbackId.value ?: 0

    init {
        _reviewDetails.addSource(_feedbackId) {
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
        _feedbackId.notifyObserver()
    }

    fun setFeedbackId(feedbackId: Int) {
        this._feedbackId.value = feedbackId
    }

    fun getUserId(): String {
        return userSession.userId
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}