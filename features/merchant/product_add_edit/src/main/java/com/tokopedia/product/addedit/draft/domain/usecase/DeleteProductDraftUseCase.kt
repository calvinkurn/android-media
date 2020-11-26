package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DeleteProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Boolean>() {

    companion object{
        fun createRequestParams(productId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, productId)
            return requestParams
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Boolean {
        val param = params.getLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE)
        return draftRepository.deleteDraft(param)
    }

}