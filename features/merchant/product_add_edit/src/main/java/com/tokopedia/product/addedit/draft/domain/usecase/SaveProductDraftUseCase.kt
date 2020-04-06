package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SaveProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Long>() {

    companion object{
        fun createRequestParams(product: ProductInputModel, productId: Long, isUploading: Boolean): RequestParams {
            val params = RequestParams.create()
            params.putObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL, product)
            params.putLong(AddEditProductDraftConstant.PREV_DRAFT_ID, productId)
            params.putBoolean(AddEditProductDraftConstant.IS_UPLOADING, isUploading)
            return params
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    private fun isInputProductNotNull() = params.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) != null

    private fun isUploadProductDomainModel() = params.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) is ProductInputModel

    override suspend fun executeOnBackground(): Long {
        val product: ProductInputModel
        if(isInputProductNotNull() && isUploadProductDomainModel()) {
            product = params.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) as ProductInputModel
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