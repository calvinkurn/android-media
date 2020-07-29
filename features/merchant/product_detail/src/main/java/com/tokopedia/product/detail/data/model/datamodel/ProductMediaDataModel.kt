package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMediaDataModel(
        val type: String = "",
        val name: String = "",
        var listOfMedia: List<MediaDataModel> = listOf(),
        var shouldRefreshViewPagger: Boolean = true,
        var shouldRenderImageVariant: Boolean = true,
        var shouldShowImageReview: Boolean = false
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

data class MediaDataModel(
        val id:String = "",
        val type: String = "",
        val url300: String = "",
        val urlOriginal: String = "",
        val urlThumbnail: String = "",
        val mediaDescription: String = "",
        val videoUrl: String = "",
        val isAutoPlay: Boolean = false
)