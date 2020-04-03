package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.common.domain.model.params.add.Product
import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class FetchProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Product>() {

    companion object {
        fun createRequestParams(productId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, productId)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<Product> {
        val param = requestParams?.getLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE) ?: 0L
        return draftRepository.getDraft(param)
    }
}