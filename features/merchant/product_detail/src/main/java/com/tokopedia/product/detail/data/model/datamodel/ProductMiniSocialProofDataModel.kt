package com.tokopedia.product.detail.data.model.datamodel

import android.content.Context
import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.productThousandFormatted
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

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMiniSocialProofDataModel) {
            wishlistCount == newData.wishlistCount &&
                    viewCount == newData.viewCount &&
                    shouldRenderSocialProof == newData.shouldShowSingleViewSocialProof()
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

    private fun firstPositionData(): Pair<String, Int> {
        return when {
            paymentVerifiedCount != 0 -> PAYMENT_VERIFIED to paymentVerifiedCount
            wishlistCount != 0 -> WISHLIST to wishlistCount
            viewCount != 0 -> VIEW_COUNT to viewCount
            else -> "" to 0
        }
    }

    /**
     * Social proof mini should only show 3 of this, with hierarchy
     * When it only contains 1 data, it will show single line social proof
     */
    val getLastThreeHirarchyData: List<Pair<String, Int>>
        get() = listOf(firstPositionData(),
                RATING to ratingCount,
                TALK to talkCount)
                .filter { it.second > 0 }
                .take(3)

    fun shouldShowSingleViewSocialProof(): Boolean {
        return talkCount == 0 && ratingCount == 0
    }

    fun generateFirstSocialProofText(context: Context): String {
        if (firstPositionData().second == 0) return ""
        return when (firstPositionData().first) {
            PAYMENT_VERIFIED -> {
                context.getString(R.string.label_terjual_builder, firstPositionData().second.productThousandFormatted())
            }
            WISHLIST -> {
                context.getString(R.string.label_wishlist_builder, firstPositionData().second.productThousandFormatted())
            }
            VIEW_COUNT -> {
                context.getString(R.string.label_view_builder, firstPositionData().second.productThousandFormatted())
            }
            else -> {
                ""
            }
        }
    }

    fun generateSingleView(context: Context): String {
        if (firstPositionData().second == 0) return ""
        return when (firstPositionData().first) {
            PAYMENT_VERIFIED -> {
                context.getString(R.string.terjual_single_text_template_builder, firstPositionData().second.productThousandFormatted())
            }
            WISHLIST -> {
                context.getString(R.string.wishlist_single_text_template_builder, firstPositionData().second.productThousandFormatted())
            }
            VIEW_COUNT -> {
                context.getString(R.string.view_single_text__template_builder, firstPositionData().second.productThousandFormatted())
            }
            else -> {
                ""
            }
        }
    }

    fun isFirstData(data: Pair<String, Int>): Boolean {
        return when (data.first) {
            PAYMENT_VERIFIED, WISHLIST, VIEW_COUNT -> true
            else -> {
                false
            }
        }
    }
}