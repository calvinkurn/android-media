package com.tokopedia.product.addedit.imagepicker.domain.interactor

import com.tokopedia.product.manage.common.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
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

    override fun createObservable(params: RequestParams?): Observable<Long> {
        val isInputProductNotNull= params?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) != null
        val isUploadProductDomainModel = params?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) is ProductDraft

        val product: ProductDraft = if(isInputProductNotNull && isUploadProductDomainModel) {
            params?.getObject(AddEditProductDraftConstant.UPLOAD_PRODUCT_INPUT_MODEL) as ProductDraft
        } else {
            throw RuntimeException("Input model is missing")
        }

        val prevDraftId = params.getLong(AddEditProductDraftConstant.PREV_DRAFT_ID, 0)
        val isUploading = params.getBoolean(AddEditProductDraftConstant.IS_UPLOADING, false)
        return if (prevDraftId <= 0) {
            Observable.just(draftRepository.insertDraft(product, isUploading))
        } else {
            Observable.just(draftRepository.updateDraft(prevDraftId, product, isUploading))
        }
    }

}