package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
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
        var buyerPhotosCount: Int = 0,
        var paymentVerifiedCount: Int = 0,
        var shouldRenderSocialProof: Boolean = false
) : DynamicPdpDataModel {
    companion object {
        const val RATING = "rating"
        const val TALK = "talk"
        const val PAYMENT_VERIFIED = "paymentVerified"
        const val WISHLIST = "wishlist"
        const val VIEW_COUNT = "viewCount"
        const val BUYER_PHOTOS = "buyerPhotos"
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
                    shouldRenderSocialProof == newData.shouldRenderSocialProof
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

    private fun firstPositionData(type: ProductMiniSocialProofItemType): ProductMiniSocialProofItemDataModel {
        return when {
            paymentVerifiedCount != 0 -> ProductMiniSocialProofItemDataModel(PAYMENT_VERIFIED, paymentVerifiedCount, type)
            wishlistCount != 0 -> ProductMiniSocialProofItemDataModel(WISHLIST, wishlistCount, type)
            viewCount != 0 -> ProductMiniSocialProofItemDataModel(VIEW_COUNT, viewCount, type)
            else -> ProductMiniSocialProofItemDataModel(type = type)
        }
    }

    /**
     * Social proof mini should only show 4 of this, with hierarchy
     * When it only contains 1 data, it will show single line social proof
     */
    private var socialProofData: List<ProductMiniSocialProofItemDataModel> = emptyList()

    fun shouldShowSingleViewSocialProof(): Boolean {
        return talkCount == 0 && ratingCount == 0 && buyerPhotosCount == 0
    }

    fun setSocialProofData() {
        if(shouldShowSingleViewSocialProof()) {
            socialProofData = listOf(firstPositionData(ProductMiniSocialProofItemType.ProductMiniSocialProofSingleText))
            return
        }
        socialProofData = listOf(firstPositionData(ProductMiniSocialProofItemType.ProductMiniSocialProofText),
                ProductMiniSocialProofItemDataModel(RATING, ratingCount, ProductMiniSocialProofItemType.ProductMiniSocialProofChip),
                ProductMiniSocialProofItemDataModel(BUYER_PHOTOS, buyerPhotosCount, ProductMiniSocialProofItemType.ProductMiniSocialProofChip),
                ProductMiniSocialProofItemDataModel(TALK, talkCount, ProductMiniSocialProofItemType.ProductMiniSocialProofChip))
                .filter { it.count > 0 }
                .take(4)
    }

    fun getSocialProofData(): List<ProductMiniSocialProofItemDataModel> {
        return socialProofData
    }
}