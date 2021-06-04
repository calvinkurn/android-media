package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.productThousandFormatted
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMiniSocialProofTokoNowDataModel(
        val type: String = "",
        val name: String = "",
        var stock: Int = 0,
        var rating: Float? = 0F,
        var ratingCount: Int = 0,
        var viewCount: Int = 0,
        var wishlistCount: Int = 0,
        var buyerPhotosCount: Int = 0,
        var paymentVerifiedCount: Int = 0,
        var shouldRenderSocialProof: Boolean = false
) : DynamicPdpDataModel {
    companion object {
        const val RATING = "rating"
        const val PAYMENT_VERIFIED = "paymentVerified"
        const val WISHLIST = "wishlist"
        const val VIEW_COUNT = "viewCount"
        const val BUYER_PHOTOS = "buyerPhotos"
        const val STOCK = "stock"
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

    private fun firstPositionData(type: ProductMiniSocialProofTokoNowItemType): ProductMiniSocialProofTokoNowItemDataModel {
        return when {
            paymentVerifiedCount != 0 -> ProductMiniSocialProofTokoNowItemDataModel(PAYMENT_VERIFIED, paymentVerifiedCount.productThousandFormatted(), type)
            wishlistCount != 0 -> ProductMiniSocialProofTokoNowItemDataModel(WISHLIST, wishlistCount.productThousandFormatted(), type)
            viewCount != 0 -> ProductMiniSocialProofTokoNowItemDataModel(VIEW_COUNT, viewCount.productThousandFormatted(), type)
            else -> ProductMiniSocialProofTokoNowItemDataModel(type = type)
        }
    }

    /**
     * Social proof mini should only show 4 of this, with hierarchy
     * When it only contains 1 data, it will show single line social proof
     */
    private var socialProofData: List<ProductMiniSocialProofTokoNowItemDataModel> = emptyList()

    fun shouldShowSingleViewSocialProof(): Boolean {
        return ratingCount == 0 && buyerPhotosCount == 0 && stock == 0
    }

    fun setSocialProofData() {
        if (shouldShowSingleViewSocialProof()) {
            socialProofData = listOf(firstPositionData(ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofSingleText))
            return
        }
        val socialProofBuilder = mutableListOf(
                stockData(ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofTextWithDivider),
                firstPositionData(ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofText)
        )
        appendChipIfNotZero(ratingCount.toFloat(), RATING, socialProofBuilder, rating.toString())
        appendChipIfNotZero(buyerPhotosCount.toFloat(), BUYER_PHOTOS, socialProofBuilder)
        socialProofData = socialProofBuilder.take(4)
    }

    fun getSocialProofData(): List<ProductMiniSocialProofTokoNowItemDataModel> {
        return socialProofData
    }

    private fun appendChipIfNotZero(count: Float?, type: String, list: MutableList<ProductMiniSocialProofTokoNowItemDataModel>, ratingTitle: String = ""): MutableList<ProductMiniSocialProofTokoNowItemDataModel> {
        if (count != 0F) {
            if (type == RATING) {
                list.add(ProductMiniSocialProofTokoNowItemDataModel(type, count?.productThousandFormatted()
                        ?: "", ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofChip, ratingTitle))
            } else {
                list.add(ProductMiniSocialProofTokoNowItemDataModel(type, count?.productThousandFormatted()
                        ?: "", ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofChip))
            }
        }
        return list
    }

    private fun stockData(type: ProductMiniSocialProofTokoNowItemType): ProductMiniSocialProofTokoNowItemDataModel {
        return ProductMiniSocialProofTokoNowItemDataModel(STOCK, stock.productThousandFormatted(), type)
    }
}
