package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<ProductInputModel>() {

    companion object {
        fun createRequestParams(productId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, productId)
            return requestParams
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductInputModel {
        val param = params.getLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE)
        return draftRepository.getDraft(param)
    }
}