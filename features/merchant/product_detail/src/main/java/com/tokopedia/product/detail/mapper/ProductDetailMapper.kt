package com.tokopedia.product.detail.mapper

import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductUiModel
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery

class ProductDetailMapper {

    fun mapProductDetailToProductPreview(
        data: DynamicProductInfoP1,
        position: Int = 0,
        videoLastDuration: Long = 0L,
        videoTotalDuration: Long = 0L
    ): ProductUiModel {
        return ProductUiModel(
            productId = data.parentProductId,
            content = data.data.getGalleryItems().mapIndexed { index, item ->
                ProductContentUiModel(
                    contentId = item.id,
                    selected = index == position,
                    type = when (item.type) {
                        ProductDetailGallery.Item.Type.Video -> MediaType.Video
                        ProductDetailGallery.Item.Type.Image -> MediaType.Image
                        else -> MediaType.Unknown
                    },
                    url = item.url,
                    videoLastDuration = videoLastDuration
                )
            },
            indicator = data.data.getGalleryItems().mapIndexed { index, item ->
                ProductIndicatorUiModel(
                    indicatorId = item.id,
                    selected = index == position,
                    variantName = item.tag.orEmpty(),
                    type = when (item.type) {
                        ProductDetailGallery.Item.Type.Video -> MediaType.Video
                        ProductDetailGallery.Item.Type.Image -> MediaType.Image
                        else -> MediaType.Unknown
                    },
                    thumbnailUrl = item.thumbnailUrl,
                    videoTotalDuration = videoTotalDuration
                )
            }
        )
    }
}
