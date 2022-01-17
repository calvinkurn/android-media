package com.tokopedia.review.common.reviewreplyinsert.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.reviewreplyinsert.domain.mapper.ReviewReplyInsertMapper
import com.tokopedia.review.common.reviewreplyinsert.domain.usecase.ReviewReplyInsertUseCase
import com.tokopedia.review.common.reviewreplyinsert.presentation.model.ReviewReplyInsertUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewReplyInsertViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val reviewReplyInsertUseCase: ReviewReplyInsertUseCase
): BaseViewModel(coroutineDispatchers.main) {

    private val _insertReviewReply = MutableLiveData<Result<ReviewReplyInsertUiModel>>()
    val insertReviewReply: LiveData<Result<ReviewReplyInsertUiModel>>
        get() = _insertReviewReply

    fun insertReviewReply(feedbackId: String, responseMessage: String) {
        launchCatchError(block = {
            val responseInsertReply = withContext(coroutineDispatchers.io) {
                reviewReplyInsertUseCase.params = ReviewReplyInsertUseCase.createParams(
                    feedbackId,
                    responseMessage
                )
                ReviewReplyInsertMapper.mapToInsertReplyUiModel(reviewReplyInsertUseCase.executeOnBackground())
            }
            _insertReviewReply.value = Success(responseInsertReply)
        }, onError = {
            _insertReviewReply.value = Fail(it)
        })
    }
}