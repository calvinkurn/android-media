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
        var variantOptionIdScrollAnchor: String = ""
) : DynamicPdpDataModel {
    companion object {
        const val VIDEO_TYPE = "video"
        const val IMAGE_TYPE = "image"
    }

    fun isMediaContainsVideo(): Boolean = listOfMedia.any { it.type == VIDEO_TYPE }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMediaDataModel) {
            listOfMedia.hashCode() == newData.listOfMedia.hashCode()
                    && initialScrollPosition == newData.initialScrollPosition
                    && variantOptionIdScrollAnchor == newData.variantOptionIdScrollAnchor
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
            if (variantOptionIdScrollAnchor != newData.variantOptionIdScrollAnchor) {
                bundle.putInt(
                        ProductDetailConstant.DIFFUTIL_PAYLOAD,
                        ProductDetailConstant.PAYLOAD_SCROLL_IMAGE_VARIANT
                )

                return bundle
            }
            null
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