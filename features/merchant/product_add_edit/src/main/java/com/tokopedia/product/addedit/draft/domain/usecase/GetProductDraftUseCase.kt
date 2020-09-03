package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductDraftUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<ProductDraft>() {

    companion object {
        fun createRequestParams(draftId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(_root_ide_package_.com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DRAFT_PRODUCT_ID, draftId)
            return requestParams
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductDraft {
        val param = params.getLong(_root_ide_package_.com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE)
        return draftRepository.getDraft(param)
    }
}