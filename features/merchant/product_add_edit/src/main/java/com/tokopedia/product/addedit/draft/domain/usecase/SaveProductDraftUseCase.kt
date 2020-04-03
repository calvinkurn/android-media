package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
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

    private fun isInputProductNotNull(requestParams: RequestParams?) = requestParams?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) != null

    private fun isUploadProductDomainModel(requestParams: RequestParams?) = requestParams?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) is ProductInputModel

    override fun createObservable(requestParams: RequestParams?): Observable<Long> {
        val product: ProductInputModel
        if(isInputProductNotNull(requestParams) && isUploadProductDomainModel(requestParams)) {
            product = requestParams?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) as ProductInputModel
        } else {
            throw RuntimeException("Input model is missing")
        }

        val prevDraftId = requestParams.getLong(AddEditProductDraftConstant.PREV_DRAFT_ID, 0)
        val isUploading = requestParams.getBoolean(AddEditProductDraftConstant.IS_UPLOADING, false)
        return if (prevDraftId <= 0) {
            draftRepository.saveDraft(product, isUploading)
        } else {
            draftRepository.updateDraft(prevDraftId, product, isUploading)
        }
    }



}