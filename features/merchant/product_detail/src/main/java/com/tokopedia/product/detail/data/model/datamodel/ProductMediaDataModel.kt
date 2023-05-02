package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMediaDataModel(
    val type: String = "",
    val name: String = "",
    var listOfMedia: List<MediaDataModel> = listOf(),
    var initialScrollPosition: Int = -1,
    var variantOptionIdScrollAnchor: String = "",
    var shouldUpdateImage: Boolean = false,
    var shouldAnimateLabel: Boolean = true,
    var containerType: MediaContainerType = MediaContainerType.Square
) : DynamicPdpDataModel {
    companion object {
        const val VIDEO_TYPE = "video"
        const val IMAGE_TYPE = "image"
    }

    fun isMediaContainsVideo(): Boolean = listOfMedia.any { it.type == VIDEO_TYPE }

    /**
     * Not Found will return 0
     */
    fun indexOfSelectedVariantOptionId(): Int {
        return listOfMedia.indexOfFirst {
            it.variantOptionId == variantOptionIdScrollAnchor
        }.takeIf { it > -1 } ?: 0
    }

    fun getScrollPosition(): Int {
        val optionIdAnchor = variantOptionIdScrollAnchor
        return if (optionIdAnchor.isNotEmpty() && shouldUpdateImage) {
            listOfMedia.indexOfFirst {
                it.variantOptionId == optionIdAnchor
            }.takeIf { it > -1 } ?: 0
        } else {
            initialScrollPosition
        }
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMediaDataModel) {
            listOfMedia.hashCode() == newData.listOfMedia.hashCode() &&
                initialScrollPosition == newData.initialScrollPosition &&
                variantOptionIdScrollAnchor == newData.variantOptionIdScrollAnchor
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductMediaDataModel) {
            if (listOfMedia.hashCode() != newData.listOfMedia.hashCode()) {
                bundle.putInt(
                    ProductDetailConstant.DIFFUTIL_PAYLOAD,
                    ProductDetailConstant.PAYLOAD_MEDIA_UPDATE
                )
            } else if (newData.variantOptionIdScrollAnchor.isNotEmpty() && variantOptionIdScrollAnchor != newData.variantOptionIdScrollAnchor) {
                bundle.putInt(
                    ProductDetailConstant.DIFFUTIL_PAYLOAD,
                    ProductDetailConstant.PAYLOAD_SCROLL_IMAGE_VARIANT
                )
            }
            return bundle.takeIf { !it.isEmpty }
        } else {
            null
        }
    }
}

data class MediaDataModel(
    val id: String = "",
    val type: String = "",
    val url300: String = "",
    val urlOriginal: String = "",
    val urlThumbnail: String = "",
    val mediaDescription: String = "",
    val videoUrl: String = "",
    val isAutoPlay: Boolean = false,
    val variantOptionId: String = ""
) {
    fun isVideoType(): Boolean = type == ProductMediaDataModel.VIDEO_TYPE
}

data class ThumbnailDataModel(
    val media: MediaDataModel = MediaDataModel(),
    val isSelected: Boolean = false,
    val impressHolder: ImpressHolder = ImpressHolder()
)

sealed class MediaContainerType(val type: String, val ratio: String) {
    object Square : MediaContainerType(type = "square", "H,1:1")
    object Portrait : MediaContainerType(type = "portrait", "H,4:5")
}

internal fun String?.asMediaContainerType(): MediaContainerType = when (this) {
    MediaContainerType.Portrait.type -> MediaContainerType.Portrait
    else -> MediaContainerType.Square
}
