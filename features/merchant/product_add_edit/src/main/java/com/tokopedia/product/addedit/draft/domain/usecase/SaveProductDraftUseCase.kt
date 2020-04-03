package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.common.domain.model.params.add.Product
import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SaveProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Long>() {

    companion object{
        fun createRequestParams(product: Product, productId: Long, isUploading: Boolean): RequestParams {
            val params = RequestParams.create()
            params.putObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL, product)
            params.putLong(AddEditProductDraftConstant.PREV_DRAFT_ID, productId)
            params.putBoolean(AddEditProductDraftConstant.IS_UPLOADING, isUploading)
            return params
        }
    }

    private fun isInputProductNotNull(requestParams: RequestParams?) = requestParams?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) != null

    private fun isUploadProductDomainModel(requestParams: RequestParams?) = requestParams?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) is Product

    override fun createObservable(requestParams: RequestParams?): Observable<Long> {
        val product: Product
        if(isInputProductNotNull(requestParams) && isUploadProductDomainModel(requestParams)) {
            product = requestParams?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) as Product
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