package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel.ProductPreviewSourceName
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.SocialProofData

class ProductPreviewSourceMapper(private val productId: String) {

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
