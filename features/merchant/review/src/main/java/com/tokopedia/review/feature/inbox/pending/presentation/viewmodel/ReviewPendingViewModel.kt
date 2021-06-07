package com.tokopedia.review.feature.inbox.pending.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.review.feature.ovoincentive.usecase.GetProductIncentiveOvo
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponse
import com.tokopedia.review.feature.inbox.pending.domain.usecase.ProductrevMarkAsSeenUseCase
import com.tokopedia.review.feature.inbox.pending.domain.usecase.ProductrevWaitForFeedbackUseCase
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewPendingViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val productrevWaitForFeedbackUseCase: ProductrevWaitForFeedbackUseCase,
        private val getProductIncentiveOvo: GetProductIncentiveOvo,
        private val markAsSeenUseCase: ProductrevMarkAsSeenUseCase
) : BaseViewModel(dispatchers.io) {

    private val _reviewList = MutableLiveData<ReviewViewState<ProductrevWaitForFeedbackResponse>>()
    val reviewList: LiveData<ReviewViewState<ProductrevWaitForFeedbackResponse>>
        get() = _reviewList

    private var _incentiveOvo = MutableLiveData<Result<ProductRevIncentiveOvoDomain>?>()
    val incentiveOvo: LiveData<Result<ProductRevIncentiveOvoDomain>?>
        get() = _incentiveOvo

    fun getReviewData(page: Int, isRefresh: Boolean = false) {
        if (isRefresh) {
            _reviewList.value = LoadingView()
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

    fun getProductIncentiveOvo() {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                getProductIncentiveOvo.getIncentiveOvo()
            }
            if (data == null) {
                _incentiveOvo.postValue(null)
            } else {
                _incentiveOvo.postValue(CoroutineSuccess(data))
            }
        }) {
            _incentiveOvo.postValue(CoroutineFail(it))
        }
    }

    fun markAsSeen(inboxReviewId: Long) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                markAsSeenUseCase.setParams(inboxReviewId)
                markAsSeenUseCase.executeOnBackground()
            }
        }) {
            // No Op
        }
    }

    fun getUserId(): String {
        return userSession.userId
    }

    fun getUserName(): String {
        return userSession.name
    }

}