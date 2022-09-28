package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.shop.flashsale.data.response.DoSellerCampaignProductSubmissionResponse
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import javax.inject.Inject

class SellerCampaignProductSubmissionMapper @Inject constructor() {

    fun map(data: DoSellerCampaignProductSubmissionResponse): ProductSubmissionResult {
        val failedProducts = data.doSellerCampaignProductSubmission.productFailed.map { product ->
            ProductSubmissionResult.FailedProduct(product.productId, product.message)
        }
        return ProductSubmissionResult(
            data.doSellerCampaignProductSubmission.isSuccess,
            data.doSellerCampaignProductSubmission.message.errorMessage,
            failedProducts
        )
    }

}