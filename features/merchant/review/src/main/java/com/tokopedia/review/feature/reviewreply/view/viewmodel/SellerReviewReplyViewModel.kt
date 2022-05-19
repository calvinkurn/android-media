package com.tokopedia.review.feature.reviewreply.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.reviewreply.domain.GetReviewTemplateListUseCase
import com.tokopedia.review.feature.reviewreply.domain.InsertTemplateReviewReplyUseCase
import com.tokopedia.review.feature.reviewreply.insert.domain.mapper.ReviewReplyInsertMapper
import com.tokopedia.review.feature.reviewreply.insert.domain.usecase.ReviewReplyInsertUseCase
import com.tokopedia.review.feature.reviewreply.insert.presentation.model.ReviewReplyInsertUiModel
import com.tokopedia.review.feature.reviewreply.update.domain.mapper.ReviewReplyUpdateMapper
import com.tokopedia.review.feature.reviewreply.update.domain.usecase.ReviewReplyUpdateUseCase
import com.tokopedia.review.feature.reviewreply.update.presenter.model.ReviewReplyUpdateUiModel
import com.tokopedia.review.feature.reviewreply.util.mapper.SellerReviewReplyMapper
import com.tokopedia.review.feature.reviewreply.view.model.InsertTemplateReplyUiModel
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SellerReviewReplyViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getReviewTemplateListUseCase: GetReviewTemplateListUseCase,
    private val reviewReplyInsertUseCase: ReviewReplyInsertUseCase,
    private val reviewReplyUpdateUseCase: ReviewReplyUpdateUseCase,
    private val insertTemplateReviewReplyUseCase: InsertTemplateReviewReplyUseCase)
    : BaseViewModel(dispatcherProvider.main) {

    private val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    val replyTime: String
        get() = SimpleDateFormat(DATE_REVIEW_FORMAT, Locale.getDefault()).format(Calendar.getInstance().time)

    private val _updateReviewReply = MutableLiveData<Result<ReviewReplyUpdateUiModel>>()
    val updateReviewReply: LiveData<Result<ReviewReplyUpdateUiModel>>
        get() = _updateReviewReply

    private val _insertReviewReply = MutableLiveData<Result<ReviewReplyInsertUiModel>>()
    val insertReviewReply: LiveData<Result<ReviewReplyInsertUiModel>>
        get() = _insertReviewReply

    private val _reviewTemplate = MutableLiveData<Result<List<ReplyTemplateUiModel>>>()
    val reviewTemplate: LiveData<Result<List<ReplyTemplateUiModel>>>
        get() = _reviewTemplate

    private val _insertTemplateReply = MutableLiveData<Result<InsertTemplateReplyUiModel>>()
    val insertTemplateReply: LiveData<Result<InsertTemplateReplyUiModel>>
        get() = _insertTemplateReply

    fun getTemplateListReply(shopId: String) {
        launchCatchError(block = {
            val reviewTemplateList = withContext(dispatcherProvider.io) {
                getTemplateList(shopId)
            }
            _reviewTemplate.value = Success(reviewTemplateList)
        }, onError = {
            _reviewTemplate.value = Fail(it)
        })
    }

    fun insertReviewReply(feedbackId: String, responseMessage: String) {
        launchCatchError(block = {
            val responseInsertReply = withContext(dispatcherProvider.io) {
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

    fun insertTemplateReviewReply(shopID: String, title: String, message: String) {
        launchCatchError(block = {
            val responseInsertTemplate = withContext(dispatcherProvider.io) {
                insertTemplateReviewReplyUseCase.params = InsertTemplateReviewReplyUseCase.createParams(
                        shopID,
                        title,
                        message)
                SellerReviewReplyMapper.mapToInsertTemplateReplyUiModel(insertTemplateReviewReplyUseCase.executeOnBackground())
            }
            _insertTemplateReply.value = Success(responseInsertTemplate)
        }, onError = {
            _insertTemplateReply.value = Fail(it)
        })
    }

    fun updateReviewReply(feedbackId: String, responseMessage: String) {
        launchCatchError(block = {
            val responseUpdateReply = withContext(dispatcherProvider.io) {
                reviewReplyUpdateUseCase.params = ReviewReplyUpdateUseCase.createParams(
                    feedbackId,
                    responseMessage
                )
                ReviewReplyUpdateMapper.mapToUpdateReplyUiModel(reviewReplyUpdateUseCase.executeOnBackground())
            }
            _updateReviewReply.value = Success(responseUpdateReply)
        }, onError = {
            _updateReviewReply.value = Fail(it)
        })
    }

    private suspend fun getTemplateList(shopId: String): List<ReplyTemplateUiModel> {
        getReviewTemplateListUseCase.params = GetReviewTemplateListUseCase.createParams(shopId)
        return SellerReviewReplyMapper.mapToItemTemplateUiModel(getReviewTemplateListUseCase.executeOnBackground())
    }
}
