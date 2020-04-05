package com.tokopedia.product.addedit.draft.domain.usecase

import androidx.lifecycle.LiveData
import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class FetchProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<LiveData<ProductInputModel>>() {

    companion object {
        fun createRequestParams(productId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, productId)
            return requestParams
        }
    }

    override suspend fun executeOnBackground(): LiveData<ProductInputModel> {
        val param = useCaseRequestParams.getLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE)
        return draftRepository.getDraft(param)
    }
}