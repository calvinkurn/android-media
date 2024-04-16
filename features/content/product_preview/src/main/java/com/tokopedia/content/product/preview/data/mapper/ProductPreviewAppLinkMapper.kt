package com.tokopedia.content.product.preview.data.mapper

import android.content.Intent
import android.os.Bundle
import com.tokopedia.content.product.preview.utils.ATTACHMENT_ID_QUERY_PARAMS
import com.tokopedia.content.product.preview.utils.PRODUCT_PREVIEW_SOURCE
import com.tokopedia.content.product.preview.utils.REVIEW_ID_QUERY_PARAMS
import com.tokopedia.content.product.preview.utils.SHARE_KEY
import com.tokopedia.content.product.preview.utils.SOURCE_QUERY_PARAMS
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel

class ProductPreviewAppLinkMapper(val productId: String) {

    fun mapSourceAppLink(intent: Intent): Bundle? {
        val entryPoint = intent.data?.getQueryParameter(SOURCE_QUERY_PARAMS).orEmpty()
        val reviewId = intent.data?.getQueryParameter(REVIEW_ID_QUERY_PARAMS)
        val attachmentId = intent.data?.getQueryParameter(ATTACHMENT_ID_QUERY_PARAMS)
        val source = when {
            reviewId == null -> return null
            entryPoint == SHARE_KEY -> ProductPreviewSourceModel.ShareSourceData(
                reviewSourceId = reviewId,
                attachmentSourceId = attachmentId
            )
            attachmentId == null -> {
                ProductPreviewSourceModel.ReviewSourceData(reviewSourceId = reviewId)
            }
            else -> {
                ProductPreviewSourceModel.ReviewSourceData(
                    reviewSourceId = reviewId,
                    attachmentSourceId = attachmentId
                )
            }
        }
        val sourceModel = ProductPreviewSourceModel(
            productId = productId,
            sourceName = ProductPreviewSourceModel.ProductPreviewSourceName.getByValue(entryPoint),
            source = source
        )
        return Bundle().apply {
            putParcelable(PRODUCT_PREVIEW_SOURCE, sourceModel)
        }
    }

}
