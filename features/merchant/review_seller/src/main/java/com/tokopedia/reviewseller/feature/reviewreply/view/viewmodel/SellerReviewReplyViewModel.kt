package com.tokopedia.reviewseller.feature.reviewreply.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.feature.reviewreply.domain.GetReviewTemplateListUseCase
import com.tokopedia.reviewseller.feature.reviewreply.util.mapper.SellerReviewReplyMapper
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerReviewReplyViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val getReviewTemplateListUseCase: GetReviewTemplateListUseCase)
    : BaseViewModel(dispatcherProvider.main()) {

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

    private suspend fun getTemplateList(shopId: Int): List<ReplyTemplateUiModel> {
        getReviewTemplateListUseCase.params = GetReviewTemplateListUseCase.createParams(shopId)
        return SellerReviewReplyMapper.mapToItemTemplateUiModel(getReviewTemplateListUseCase.executeOnBackground())
    }
}
