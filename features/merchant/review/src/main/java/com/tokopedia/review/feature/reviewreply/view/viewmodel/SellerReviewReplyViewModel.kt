package com.tokopedia.review.feature.reviewreply.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.review.feature.reviewreply.domain.GetReviewTemplateListUseCase
import com.tokopedia.review.feature.reviewreply.domain.InsertSellerResponseUseCase
import com.tokopedia.review.feature.reviewreply.domain.InsertTemplateReviewReplyUseCase
import com.tokopedia.review.feature.reviewreply.domain.UpdateSellerResponseUseCase
import com.tokopedia.review.feature.reviewreply.util.mapper.SellerReviewReplyMapper
import com.tokopedia.review.feature.reviewreply.view.model.InsertReplyResponseUiModel
import com.tokopedia.review.feature.reviewreply.view.model.InsertTemplateReplyUiModel
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.review.feature.reviewreply.view.model.UpdateReplyResponseUiModel
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
    private val insertSellerResponseUseCase: InsertSellerResponseUseCase,
    private val updateSellerResponseUseCase: UpdateSellerResponseUseCase,
    private val insertTemplateReviewReplyUseCase: InsertTemplateReviewReplyUseCase)
    : BaseViewModel(dispatcherProvider.main) {

    private val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    val replyTime: String
        get() = SimpleDateFormat(DATE_REVIEW_FORMAT, Locale.getDefault()).format(Calendar.getInstance().time)

    private val _updateReviewReply = MutableLiveData<Result<UpdateReplyResponseUiModel>>()
    val updateReviewReply: LiveData<Result<UpdateReplyResponseUiModel>>
        get() = _updateReviewReply

    private val _insertReviewReply = MutableLiveData<Result<InsertReplyResponseUiModel>>()
    val insertReviewReply: LiveData<Result<InsertReplyResponseUiModel>>
        get() = _insertReviewReply

    private val _reviewTemplate = MutableLiveData<Result<List<ReplyTemplateUiModel>>>()
    val reviewTemplate: LiveData<Result<List<ReplyTemplateUiModel>>>
        get() = _reviewTemplate

    private val _insertTemplateReply = MutableLiveData<Result<InsertTemplateReplyUiModel>>()
    val insertTemplateReply: LiveData<Result<InsertTemplateReplyUiModel>>
        get() = _insertTemplateReply

    fun getTemplateListReply(shopId: Int) {
        launchCatchError(block = {
            val reviewTemplateList = withContext(dispatcherProvider.io) {
                getTemplateList(shopId)
            }
            _reviewTemplate.value = Success(reviewTemplateList)
        }, onError = {
            _reviewTemplate.value = Fail(it)
        })
    }

    fun insertReviewReply(reviewId: Int, productId: Int, shopId: Int, responseMessage: String) {
        launchCatchError(block = {
            val responseInsertReply = withContext(dispatcherProvider.io) {
                insertSellerResponseUseCase.params = InsertSellerResponseUseCase.createParams(
                        reviewId,
                        productId,
                        shopId,
                        responseMessage)
                SellerReviewReplyMapper.mapToInsertReplyUiModel(insertSellerResponseUseCase.executeOnBackground())
            }
            _insertReviewReply.value = Success(responseInsertReply)
        }, onError = {
            _insertReviewReply.value = Fail(it)
        })
    }

    fun insertTemplateReviewReply(shopID: Int, title: String, message: String) {
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

    fun updateReviewReply(feedbackId: Int, responseMessage: String) {
        launchCatchError(block = {
            val responseUpdateReply = withContext(dispatcherProvider.io) {
                updateSellerResponseUseCase.params = UpdateSellerResponseUseCase.createParams(
                        feedbackId,
                        responseMessage)
                SellerReviewReplyMapper.mapToUpdateReplyUiModel(updateSellerResponseUseCase.executeOnBackground())
            }
            _updateReviewReply.value = Success(responseUpdateReply)
        }, onError = {
            _updateReviewReply.value = Fail(it)
        })
    }

    private suspend fun getTemplateList(shopId: Int): List<ReplyTemplateUiModel> {
        getReviewTemplateListUseCase.params = GetReviewTemplateListUseCase.createParams(shopId)
        return SellerReviewReplyMapper.mapToItemTemplateUiModel(getReviewTemplateListUseCase.executeOnBackground())
    }
}
