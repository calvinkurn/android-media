package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * This For "Deskripsi" and "Informasi Produk"
 */
data class ProductInfoDataModel(
        val type: String = "",
        val name: String = "",

        val data: List<ProductInfoContent>? = null,
        var youtubeVideos: List<YoutubeVideo> = listOf()
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductInfoDataModel) {
            data.hashCode() == newData.data.hashCode()
                    && youtubeVideos.hashCode() == newData.youtubeVideos.hashCode()
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }
}

data class ProductInfoContent(
        val row: String = "",
        val listOfContent: List<Content> = listOf()
)