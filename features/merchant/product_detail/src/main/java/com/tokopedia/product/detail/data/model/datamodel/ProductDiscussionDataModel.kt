package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * For "Diskusi Produk"
 */
data class ProductDiscussionDataModel(
        val dataLayout: List<ComponentData> = listOf(),
        val type: String = "",
        var latestTalk: Talk? = null,
        var talkCount: Int = 0,
        var shopId: String = ""
) : DynamicPDPDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}