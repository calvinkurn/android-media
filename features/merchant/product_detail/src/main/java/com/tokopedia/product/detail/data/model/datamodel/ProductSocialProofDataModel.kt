package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSocialProofDataModel(
        val type: String = "",
        val name: String = "",
        //P2
        var rating: Float? = null,
        var wishListCount: Int = 0,
        var stats: Stats? = null,
        var txStats: TxStatsDynamicPdp? = null,
        var isSocialProofPv: Boolean = false
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder =  ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}