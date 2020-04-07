package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateLoadingProductDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Boolean>() {
    companion object {
        const val DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID"
        const val IS_UPLOADING = "IS_UPLOADING"

        fun createRequestParams(productId: Long, isUploading: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(DRAFT_PRODUCT_ID, productId)
            requestParams.putBoolean(IS_UPLOADING, isUploading)
            return requestParams
        }

        fun createRequestParamsUpdateAll(isUploading: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putBoolean(IS_UPLOADING, isUploading)
            return requestParams
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Boolean {
        val paramProductId = params.getLong(DRAFT_PRODUCT_ID, 0)
        val paramIsBoolean = params.getBoolean(IS_UPLOADING, false)
        return draftRepository.updateLoadingStatus(paramProductId, paramIsBoolean)
    }
}