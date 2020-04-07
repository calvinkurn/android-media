package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.manage.common.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductDraftUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<ProductDraft>() {

    companion object {
        fun createRequestParams(productId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, productId)
            return requestParams
        }
    }

    override suspend fun executeOnBackground(): ProductDraft {
        val param = useCaseRequestParams.getLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE)
        return draftRepository.getDraft(param)
    }
}