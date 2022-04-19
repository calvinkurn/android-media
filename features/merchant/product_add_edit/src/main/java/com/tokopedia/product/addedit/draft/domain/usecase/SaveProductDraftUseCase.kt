package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SaveProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Long>() {

    companion object{
        fun createRequestParams(productDraft: ProductDraft, productId: Long, isUploading: Boolean): RequestParams {
            val params = RequestParams.create()
            params.putObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL, productDraft)
            params.putLong(AddEditProductDraftConstant.PREV_DRAFT_ID, productId)
            params.putBoolean(AddEditProductDraftConstant.IS_UPLOADING, isUploading)
            return params
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    private fun isInputProductNotNull() = params.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) != null

    private fun isUploadProductDomainModel() = params.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) is ProductDraft

    override suspend fun executeOnBackground(): Long {
        val product: ProductDraft = if(isInputProductNotNull() && isUploadProductDomainModel()) {
            params.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) as ProductDraft
        } else {
            throw RuntimeException("Input model is missing")
        }

        val prevDraftId = params.getLong(AddEditProductDraftConstant.PREV_DRAFT_ID, 0)
        val isUploading = params.getBoolean(AddEditProductDraftConstant.IS_UPLOADING, false)
        return if (prevDraftId <= 0) {
            draftRepository.insertDraft(product, isUploading)
        } else {
            draftRepository.updateDraft(prevDraftId, product, isUploading)
        }
    }


}