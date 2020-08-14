package com.tokopedia.review.feature.historydetails.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.feature.historydetails.data.InboxReviewInsertReputation
import com.tokopedia.review.feature.historydetails.data.InboxReviewInsertReputationResponseWrapper
import com.tokopedia.review.feature.historydetails.domain.InboxReviewInsertReputationUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDetailViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
                                                private val userSession: UserSessionInterface,
                                                private val productrevGetReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
                                                private val inboxReviewInsertReputationUseCase: InboxReviewInsertReputationUseCase)
    : BaseViewModel(coroutineDispatcherProvider.io()) {

    private val _feedbackId = MutableLiveData<Int>()

    private val _reviewDetails = MediatorLiveData<ReviewViewState<ProductrevGetReviewDetail>>()

    private val _submitReputationResult = MutableLiveData<ReviewViewState<Int>>()

    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    val feedbackId: Int
        get() = _feedbackId.value ?: 0

    val submitReputationResult: LiveData<ReviewViewState<Int>>
        get() = _submitReputationResult

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

    fun submitReputation(reputationId: Int, reputationScore: Int) {
        launchCatchError(block = {
            _submitReputationResult.value = LoadingView()
            val response = withContext(coroutineDispatcherProvider.io()) {
                inboxReviewInsertReputationUseCase.setParams(reputationId, reputationScore, userSession.userId.toIntOrZero())
                inboxReviewInsertReputationUseCase.executeOnBackground()
            }
            if(response.inboxReviewInsertReputation.success == 1) {
                _submitReputationResult.postValue(Success(reputationScore))
            } else {
                _submitReputationResult.postValue(Fail(Throwable()))
            }
        }) {
            _submitReputationResult.postValue(Fail(it))
        }
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}