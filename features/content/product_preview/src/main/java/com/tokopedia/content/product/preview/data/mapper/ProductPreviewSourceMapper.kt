package com.tokopedia.content.product.preview.data.mapper

import android.content.Intent
import android.os.Bundle
import com.tokopedia.content.product.preview.utils.ATTACHMENT_ID_QUERY_PARAMS
import com.tokopedia.content.product.preview.utils.PRODUCT_PREVIEW_SOURCE
import com.tokopedia.content.product.preview.utils.REVIEW_ID_QUERY_PARAMS
import com.tokopedia.content.product.preview.utils.SOURCE_QUERY_PARAMS
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel.ProductPreviewSourceName
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.SocialProofData

class ProductPreviewSourceMapper(private val productId: String) {

    fun mapSourceAppLink(intent: Intent): Bundle? {
        val entryPoint = intent.data?.getQueryParameter(SOURCE_QUERY_PARAMS).orEmpty()
        val reviewId = intent.data?.getQueryParameter(REVIEW_ID_QUERY_PARAMS)
        val attachmentId = intent.data?.getQueryParameter(ATTACHMENT_ID_QUERY_PARAMS)
        val source = when {
            reviewId == null -> return null
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
            sourceName = ProductPreviewSourceName.getByValue(entryPoint),
            source = source
        )
        return Bundle().apply {
            putParcelable(PRODUCT_PREVIEW_SOURCE, sourceModel)
        }
    }

    fun mapProductSourceModel(
        productData: ProductInfoP1,
        mediaSelectedPosition: Int,
        videoLastDuration: Long,
        videoTotalDuration: Long
    ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            sourceName = ProductPreviewSourceName.PRODUCT,
            source = ProductPreviewSourceModel.ProductSourceData(
                productSourceList = productData.data.getGalleryItems().mapIndexed { index, item ->
                    ProductMediaUiModel(
                        contentId = item.id,
                        selected = index == mediaSelectedPosition,
                        variantName = item.tag.orEmpty(),
                        type = when (item.type) {
                            ProductDetailGallery.Item.Type.Video -> MediaType.Video
                            ProductDetailGallery.Item.Type.Image -> MediaType.Image
                            else -> MediaType.Unknown
                        },
                        url = item.url,
                        thumbnailUrl = item.thumbnailUrl,
                        videoLastDuration = videoLastDuration,
                        videoTotalDuration = videoTotalDuration
                    )
                },
                hasReviewMedia = productData.data.socialProof.any {
                    it.socialProofId == SocialProofData.MEDIA_ID
                }
            )
        )
    }

    fun mapReviewSourceModel(
        reviewId: String,
        attachmentId: String
    ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            sourceName = ProductPreviewSourceName.REVIEW,
            source = ProductPreviewSourceModel.ReviewSourceData(
                reviewSourceId = reviewId,
                attachmentSourceId = attachmentId
            )
        )
    }
}
