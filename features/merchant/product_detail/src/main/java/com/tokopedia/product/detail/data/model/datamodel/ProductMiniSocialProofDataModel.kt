package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 19/05/20
 */
data class ProductMiniSocialProofDataModel(
        val type: String = "",
        val name: String = "",
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

    /**
     * Social proof mini should only show 3 of this, with hierarchy
     * When it only contains 1 data, it will show single line social proof
     */
    val getLastThreeHirarchyData: List<Pair<String, Int>>
        get() = listOf(RATING to ratingCount,
                TALK to talkCount,
                PAYMENT_VERIFIED to paymentVerifiedCount,
                WISHLIST to wishlistCount,
                VIEW_COUNT to viewCount)
                .filter { it.second > 0 }
                .take(3)

    private fun shouldShowSinglePaymentVerified(): Boolean {
        val data = getLastThreeHirarchyData
        return if (data.size == 2) {
            data.size == data.count {
                it.first == PAYMENT_VERIFIED || it.first == VIEW_COUNT
            }
        } else {
            false
        }
    }

    private fun shouldShowSingleViewCount(): Boolean {
        val data = getLastThreeHirarchyData
        return data.size == 1 && data.firstOrNull()?.first == VIEW_COUNT
    }

    fun shouldShowSingleViewSocialProof(): Boolean {
        return shouldShowSinglePaymentVerified() || shouldShowSingleViewCount()
    }
}