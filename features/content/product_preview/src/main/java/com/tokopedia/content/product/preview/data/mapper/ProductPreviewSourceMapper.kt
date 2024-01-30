package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery

class ProductPreviewSourceMapper(
    private val productId: String
) {

    fun mapProductSourceModel(
        productData: DynamicProductInfoP1,
        mediaSelectedPosition: Int,
        videoLastDuration: Long,
        videoTotalDuration: Long,
    ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            productPreviewSource = ProductPreviewSourceModel.ProductSourceData(
                productSourceList = productData.data.getGalleryItems().mapIndexed { index, item ->
                    ProductContentUiModel(
                        contentId = item.id,
                        selected = index == mediaSelectedPosition,
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
                }
            )
        )
    }

    fun mapReviewSourceModel(
        reviewId: String,
        attachmentId: String,
    ): ProductPreviewSourceModel {
        return ProductPreviewSourceModel(
            productId = productId,
            productPreviewSource = ProductPreviewSourceModel.ReviewSourceData(
                reviewSourceId = reviewId,
                attachmentSourceId = attachmentId,
            )
        )
    }

}
