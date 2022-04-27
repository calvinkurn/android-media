package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.productThousandFormatted
import com.tokopedia.product.detail.data.util.ProductDetailConstant
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
        var buyerPhotoStaticText: String = ProductDetailConstant.BUYER_IMAGE_TEXT,
        var itemSoldFmt: String = "",
        var shouldRenderSocialProof: Boolean = false,
        var socialProofData: List<ProductMiniSocialProofItemDataModel> = emptyList()
) : DynamicPdpDataModel {

    companion object {
        const val RATING = "rating"
        const val TALK = "talk"
        const val PAYMENT_VERIFIED = "paymentVerified"
        const val WISHLIST = "wishlist"
        const val VIEW_COUNT = "viewCount"
        const val BUYER_PHOTOS = "buyerPhotos"
        private const val MAX_SOCIAL_PROOF_ITEM = 4
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
            itemSoldFmt != "" -> ProductMiniSocialProofItemDataModel(PAYMENT_VERIFIED, itemSoldFmt, type)
            wishlistCount != 0 -> ProductMiniSocialProofItemDataModel(WISHLIST, wishlistCount.productThousandFormatted(), type)
            viewCount != 0 -> ProductMiniSocialProofItemDataModel(VIEW_COUNT, viewCount.productThousandFormatted(), type)
            else -> ProductMiniSocialProofItemDataModel(type = type)
        }
    }

    fun shouldShowSingleViewSocialProof(): Boolean {
        return talkCount == 0 && ratingCount == 0 && buyerPhotosCount == 0
    }

    fun setSocialProofData() {
        if (shouldShowSingleViewSocialProof()) {
            socialProofData = listOf(firstPositionData(ProductMiniSocialProofItemType.ProductMiniSocialProofSingleText))
            return
        }
        val socialProofBuilder = mutableListOf(firstPositionData(ProductMiniSocialProofItemType.ProductMiniSocialProofText))
        appendChipIfNotZero(
                count = ratingCount.toFloat(),
                type = RATING,
                list = socialProofBuilder,
                ratingTitle = rating.toString())

        appendChipIfNotZero(
                count = buyerPhotosCount.toFloat(),
                type = BUYER_PHOTOS,
                list = socialProofBuilder,
                buyerPhotosTitle = buyerPhotoStaticText)

        appendChipIfNotZero(
                count = talkCount.toFloat(),
                type = TALK,
                list = socialProofBuilder)

        socialProofData = socialProofBuilder.take(MAX_SOCIAL_PROOF_ITEM)
    }

    private fun appendChipIfNotZero(count: Float?,
                                    type: String,
                                    list: MutableList<ProductMiniSocialProofItemDataModel>,
                                    ratingTitle: String = "",
                                    buyerPhotosTitle: String = ""): MutableList<ProductMiniSocialProofItemDataModel> {
        if (count != 0F) {
            when (type) {
                RATING -> {
                    list.add(ProductMiniSocialProofItemDataModel(
                            key = type,
                            formattedCount = count?.productThousandFormatted() ?: "",
                            type = ProductMiniSocialProofItemType.ProductMiniSocialProofChip,
                            reviewTitle = ratingTitle)
                    )
                }
                BUYER_PHOTOS -> {
                    list.add(ProductMiniSocialProofItemDataModel(
                            key = type,
                            formattedCount = count?.productThousandFormatted() ?: "",
                            type = ProductMiniSocialProofItemType.ProductMiniSocialProofChip,
                            buyerPhotosTitle = buyerPhotosTitle)
                    )
                }
                else -> {
                    list.add(ProductMiniSocialProofItemDataModel(
                            key = type,
                            formattedCount = count?.productThousandFormatted() ?: "",
                            type = ProductMiniSocialProofItemType.ProductMiniSocialProofChip)
                    )
                }
            }
        }
        return list
    }
}