package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class DeleteProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Boolean>() {

    companion object{
        fun createRequestParams(productId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, productId)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<Boolean> {
        val param = requestParams?.getLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE) ?: 0L
        return draftRepository.deleteDraft(param)
    }

}