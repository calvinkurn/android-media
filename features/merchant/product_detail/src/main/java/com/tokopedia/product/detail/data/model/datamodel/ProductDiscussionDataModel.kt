package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * For "Diskusi Produk"
 */
data class ProductDiscussionDataModel(
        val type: String = "",
        val name:String = "",
        var latestTalk: Talk? = null,
        var talkCount: Int = 0,
        var shopId: String = ""
) : DynamicPDPDataModel {
    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}