package com.tokopedia.review.feature.historydetails.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.feature.historydetails.domain.InboxReviewInsertReputationUseCase
import com.tokopedia.review.feature.historydetails.presentation.mapper.ReviewDetailDataMapper
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewDetailViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatchers,
                                                private val userSession: UserSessionInterface,
                                                private val productrevGetReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
                                                private val inboxReviewInsertReputationUseCase: InboxReviewInsertReputationUseCase)
    : BaseViewModel(coroutineDispatcherProvider.io) {

    private val _feedbackId = MutableLiveData<String>()

    private val _reviewDetails = MediatorLiveData<ReviewViewState<ProductrevGetReviewDetail>>()

    private val _reviewMediaThumbnails = MediatorLiveData<ReviewMediaThumbnailUiModel>()

    private val _submitReputationResult = MutableLiveData<ReviewViewState<Int>>()

    private var reviewEdited: Boolean = false

    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    val reviewMediaThumbnails: LiveData<ReviewMediaThumbnailUiModel>
        get() = _reviewMediaThumbnails

    val feedbackId: String
        get() = _feedbackId.value ?: ""

    val submitReputationResult: LiveData<ReviewViewState<Int>>
        get() = _submitReputationResult

    init {
        _reviewDetails.addSource(_feedbackId) {
            getReviewDetails(it, true)
        }
        _reviewMediaThumbnails.addSource(_reviewDetails) {
            _reviewMediaThumbnails.value = ReviewDetailDataMapper.mapReviewDetailToReviewMediaThumbnails(it)
        }
    }

    fun getReviewDetails(feedbackId: String, isRefresh: Boolean = false) {
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

    fun setFeedbackId(feedbackId: String) {
        this._feedbackId.value = feedbackId
    }

    fun getUserId(): String {
        return userSession.userId
    }

    fun getShopId(): String {
        return reviewDetails.value.let {
            if (it is Success) {
                it.data.response.shopId
            } else ""
        }
    }

    fun submitReputation(reputationId: String, reputationScore: Int) {
        _submitReputationResult.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                inboxReviewInsertReputationUseCase.setParams(reputationId, reputationScore)
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

    fun onReviewEdited() {
        reviewEdited = true
    }

    fun isReviewEdited() = reviewEdited

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}
