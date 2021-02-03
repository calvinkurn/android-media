package com.tokopedia.review.feature.historydetails.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.review.feature.historydetails.domain.InboxReviewInsertReputationUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDetailViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatchers,
                                                private val userSession: UserSessionInterface,
                                                private val productrevGetReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
                                                private val inboxReviewInsertReputationUseCase: InboxReviewInsertReputationUseCase)
    : BaseViewModel(coroutineDispatcherProvider.io) {

    private val _feedbackId = MutableLiveData<Long>()

    private val _reviewDetails = MediatorLiveData<ReviewViewState<ProductrevGetReviewDetail>>()

    private val _submitReputationResult = MutableLiveData<ReviewViewState<Int>>()

    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    val feedbackId: Long
        get() = _feedbackId.value ?: 0L

    val submitReputationResult: LiveData<ReviewViewState<Int>>
        get() = _submitReputationResult

    init {
        _reviewDetails.addSource(_feedbackId) {
            getReviewDetails(it, true)
        }
    }

    fun getReviewDetails(feedbackId: Long, isRefresh: Boolean = false) {
        if(isRefresh) {
            _reviewDetails.value = LoadingView()
        }
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                productrevGetReviewDetailUseCase.setRequestParams(feedbackId)
                productrevGetReviewDetailUseCase.executeOnBackground()
            }
            _reviewDetails.postValue(Success(data = response.productrevGetReviewDetail, isRefresh = isRefresh))
        }) {
            _reviewDetails.postValue(Fail(it))
        }
    }

    fun retry() {
        _feedbackId.notifyObserver()
    }

    fun setFeedbackId(feedbackId: Long) {
        this._feedbackId.value = feedbackId
    }

    fun getUserId(): String {
        return userSession.userId
    }

    fun submitReputation(reputationId: Long, reputationScore: Int) {
        _submitReputationResult.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
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