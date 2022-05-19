package com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.inboxdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reviewreply.insert.domain.mapper.ReviewReplyInsertMapper
import com.tokopedia.review.feature.reviewreply.insert.domain.usecase.ReviewReplyInsertUseCase
import com.tokopedia.review.feature.reviewreply.insert.presentation.model.ReviewReplyInsertUiModel
import com.tokopedia.review.feature.reviewreply.update.domain.mapper.ReviewReplyUpdateMapper
import com.tokopedia.review.feature.reviewreply.update.domain.usecase.ReviewReplyUpdateUseCase
import com.tokopedia.review.feature.reviewreply.update.presenter.model.ReviewReplyUpdateUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InboxReputationDetailViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val reviewReplyInsertUseCase: ReviewReplyInsertUseCase,
    private val reviewReplyUpdateUseCase: ReviewReplyUpdateUseCase
): BaseViewModel(coroutineDispatchers.main) {

    private val _insertReviewReplyResult = MutableLiveData<Result<ReviewReplyInsertUiModel>>()
    val insertReviewReplyResult: LiveData<Result<ReviewReplyInsertUiModel>>
        get() = _insertReviewReplyResult

    private val _deleteReviewReplyResult = MutableLiveData<Result<ReviewReplyUpdateUiModel>>()
    val deleteReviewReplyResult: LiveData<Result<ReviewReplyUpdateUiModel>>
        get() = _deleteReviewReplyResult

    fun insertReviewReply(feedbackId: String, responseMessage: String) {
        launchCatchError(block = {
            val responseInsertReply = withContext(coroutineDispatchers.io) {
                reviewReplyInsertUseCase.params = ReviewReplyInsertUseCase.createParams(
                    feedbackId,
                    responseMessage
                )
                ReviewReplyInsertMapper.mapToInsertReplyUiModel(reviewReplyInsertUseCase.executeOnBackground())
            }
            _insertReviewReplyResult.value = Success(responseInsertReply)
        }, onError = {
            _insertReviewReplyResult.value = Fail(it)
        })
    }

    fun deleteReviewResponse(
        feedbackId: String
    ) {
        launchCatchError(block = {
            val responseInsertReply = withContext(coroutineDispatchers.io) {
                reviewReplyUpdateUseCase.params = ReviewReplyUpdateUseCase.createParams(
                    feedbackId,
                    ""
                )
                ReviewReplyUpdateMapper.mapToUpdateReplyUiModel(reviewReplyUpdateUseCase.executeOnBackground())
            }
            _deleteReviewReplyResult.value = Success(responseInsertReply)
        }, onError = {
            _deleteReviewReplyResult.value = Fail(it)
        })
    }
}