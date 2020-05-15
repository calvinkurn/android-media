package com.tokopedia.reviewseller.feature.reviewreply.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.feature.reviewreply.domain.GetReviewTemplateListUseCase
import com.tokopedia.reviewseller.feature.reviewreply.domain.InsertSellerResponseUseCase
import com.tokopedia.reviewseller.feature.reviewreply.domain.UpdateSellerResponseUseCase
import com.tokopedia.reviewseller.feature.reviewreply.util.mapper.SellerReviewReplyMapper
import com.tokopedia.reviewseller.feature.reviewreply.view.model.InsertReplyResponseUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.model.UpdateReplyResponseUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SellerReviewReplyViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val getReviewTemplateListUseCase: GetReviewTemplateListUseCase,
        private val insertSellerResponseUseCase: InsertSellerResponseUseCase,
        private val updateSellerResponseUseCase: UpdateSellerResponseUseCase)
    : BaseViewModel(dispatcherProvider.main()) {

    private val DATE_REVIEW_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    private val _updateReviewReply = MutableLiveData<Result<UpdateReplyResponseUiModel>>()
    val updateReviewReply: LiveData<Result<UpdateReplyResponseUiModel>>
        get() = _updateReviewReply

    private val _insertReviewReply = MutableLiveData<Result<InsertReplyResponseUiModel>>()
    val insertReviewReply: LiveData<Result<InsertReplyResponseUiModel>>
        get() = _insertReviewReply

    private val _reviewTemplate = MutableLiveData<Result<List<ReplyTemplateUiModel>>>()
    val reviewTemplate: LiveData<Result<List<ReplyTemplateUiModel>>>
        get() = _reviewTemplate

    fun getTemplateListReply(shopId: Int) {
        launchCatchError(block = {
            val reviewTemplateList = withContext(dispatcherProvider.io()) {
                getTemplateList(shopId)
            }
            _reviewTemplate.postValue(Success(reviewTemplateList))
        }, onError = {
            _reviewTemplate.postValue(Fail(it))
        })
    }

    fun insertReviewReply(reviewId: Int, productId: Int, shopId: Int, responseMessage: String) {
        launchCatchError(block = {
            val responseInsertReply = withContext(dispatcherProvider.io()) {
                insertSellerResponseUseCase.params = InsertSellerResponseUseCase.createParams(
                        reviewId,
                        productId,
                        shopId,
                        responseMessage)
                SellerReviewReplyMapper.mapToInsertReplyUiModel(insertSellerResponseUseCase.executeOnBackground())
            }
            _insertReviewReply.postValue(Success(responseInsertReply))
        }, onError = {
            _insertReviewReply.postValue(Fail(it))
        })
    }

    fun updateReviewReply(feedbackId: Int, responseMessage: String) {
        launchCatchError(block = {
            val responseUpdateReply = withContext(dispatcherProvider.io()) {
                updateSellerResponseUseCase.params = UpdateSellerResponseUseCase.createParams(
                        feedbackId,
                        responseMessage)
                SellerReviewReplyMapper.mapToUpdateReplyUiModel(updateSellerResponseUseCase.executeOnBackground())
            }
            _updateReviewReply.postValue(Success(responseUpdateReply))
        }, onError = {
            _updateReviewReply.postValue(Fail(it))
        })
    }

    private suspend fun getTemplateList(shopId: Int): List<ReplyTemplateUiModel> {
        getReviewTemplateListUseCase.params = GetReviewTemplateListUseCase.createParams(shopId)
        return SellerReviewReplyMapper.mapToItemTemplateUiModel(getReviewTemplateListUseCase.executeOnBackground())
    }

    fun getReplyTime(): String {
        return SimpleDateFormat(DATE_REVIEW_FORMAT, Locale.getDefault()).format(Calendar.getInstance().time)
    }
}
