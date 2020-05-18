package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSocialProofDataModel(
        val type: String = "",
        val name: String = "",
        //P2
//        var rating: Float? = null,
        var stats: Stats? = null,
        var txStats: TxStatsDynamicPdp? = null,
        var isSocialProofPv: Boolean = false,

        var rating: Float? = 0F,
        var ratingCount: Int = 0,
        var viewCount: Int = 0,
        var talkCount: Int = 0,
        var wishlistCount: Int = 0,
        var paymentVerifiedCount: Int = 0,
        var shouldRenderSocialProof: Boolean = false

) : DynamicPdpDataModel {
    companion object {
        const val RATING = "rating"
        const val TALK = "talk"
        const val PAYMENT_VERIFIED = "paymentVerified"
        const val WISHLIST = "wishlist"
        const val VIEW_COUNT = "viewCount"
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun getAvailableData(): List<Pair<String, Int>> {
        return listOf(RATING to ratingCount,
                TALK to talkCount,
                PAYMENT_VERIFIED to paymentVerifiedCount,
                WISHLIST to wishlistCount,
                VIEW_COUNT to viewCount)
                .filter { it.second > 0 }
                .take(3)
    }
}