package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMediaDataModel(
        val type: String = "",
        val name: String = "",
        var listOfMedia: List<MediaDataModel> = listOf(),
        var shouldRefreshViewPagger: Boolean = true,
        var shouldRenderImageVariant: Boolean = true
) : DynamicPdpDataModel {
    companion object {
        const val VIDEO_TYPE = "video"
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
            val isMediaTheSame = listOfMedia.hashCode() == newData.listOfMedia.hashCode()
            if (isMediaTheSame) {
                // we dont want to re-render the data if its bind because the data are same
                newData.shouldRefreshViewPagger = false
            }
            isMediaTheSame
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
            if (newData.shouldRefreshViewPagger) {
                return null
            }

            if (shouldRenderImageVariant != newData.shouldRenderImageVariant) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_UPDATE_IMAGE)
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
        val isAutoPlay: Boolean = false
) {
    fun isVideoType(): Boolean = type == ProductMediaDataModel.VIDEO_TYPE
}