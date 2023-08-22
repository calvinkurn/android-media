package com.tokopedia.stories.usecase.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.stories.usecase.response.StoriesProductResponse.Data.Campaign.Restriction.Companion.FOLLOWERS_ONLY_RESTRICTION

/**
 * @author by astidhiyaa on 21/08/23
 */
data class StoriesProductResponse(
    @SerializedName("contentStoryProducts")
    val data: Data
) {
    data class Data(
        @SerializedName("products")
        val products: List<Product> = emptyList(),

        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),

        @SerializedName("nextCursor")
        val nextCursor: String = "",

        @SerializedName("hasVoucher")
        val hasVoucher: Boolean = false
    ) {
        data class Product(
            @SerializedName("id")
            val id: String = "",

            @SerializedName("hasVariant")
            val hasVariant: Boolean = false,

            @SerializedName("isParent")
            val isParent: Boolean = false,

            @SerializedName("parentID")
            val parentID: String = "",

            @SerializedName("name")
            val name: String = "",

            @SerializedName("imageURL")
            val imageUrl: String = "",

            @SerializedName("webLink")
            val webLink: String = "",

            @SerializedName("appLink")
            val appLink: String = "",

            @SerializedName("isDiscount")
            val isDiscount: Boolean = false,

            @SerializedName("discount")
            val discount: Int = 0,

            @SerializedName("discountFmt")
            val discountFmt: String = "",

            @SerializedName("price")
            val price: Int = 0,

            @SerializedName("priceDiscount")
            val priceDiscount: Int = 0,

            @SerializedName("priceDiscountFmt")
            val priceDiscountFmt: String = "",

            @SerializedName("priceFmt")
            val priceFmt: String = "",

            @SerializedName("priceOriginal")
            val priceOriginal: Int = 0,

            @SerializedName("priceOriginalFmt")
            val priceOriginalFmt: String = "",

            @SerializedName("priceMasked")
            val priceMasked: Float = 0f,

            @SerializedName("priceMaskedFmt")
            val priceMaskedFmt: String = "",

            @SerializedName("isBebasOngkir")
            val isBebasOngkir: Boolean = false,

            @SerializedName("bebasOngkirStatus")
            val bebasOngkirStatus: String = "",

            @SerializedName("bebasOngkirURL")
            val bebasOngkirURL: String = "",

            @SerializedName("totalSold")
            val totalSold: Int = 0,

            @SerializedName("totalSoldFmt")
            val totalSoldFmt: String = "",

            @SerializedName("stockWording")
            val stockWording: String = "",

            @SerializedName("stockSoldPercentage")
            val stockSoldPercentage: Float = 0f,

            @SerializedName("isCartable")
            val cartable: Boolean = false,

            @SerializedName("isWishlisted")
            val isWishlisted: Boolean = false,

            @SerializedName("isStockAvailable")
            val isStockAvailable: Boolean = false,
        )

        data class Campaign(
            @SerializedName("id")
            val id: String = "",

            @SerializedName("status")
            val status: String = "",

            @SerializedName("name")
            val name: String = "",

            @SerializedName("shortName")
            val shortName: String = "",

            @SerializedName("startTime")
            val startTime: String = "",

            @SerializedName("endTime")
            val endTime: String = "",

            @SerializedName("restrictions")
            val restrictions: List<Restriction> = emptyList(),
        ) {
            val isFollowRestriction: Boolean
                get() = restrictions.firstOrNull()?.let {
                    it.label == FOLLOWERS_ONLY_RESTRICTION && it.isActive
                }.orFalse()

            data class Restriction(
                @SerializedName("label")
                val label: String = "",

                @SerializedName("isActive")
                val isActive: Boolean = false,
            ) {
                companion object {
                    const val FOLLOWERS_ONLY_RESTRICTION = "followers_only"
                }
            }
        }
    }
}


