package com.tokopedia.feedcomponent.data.feedrevamp


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("Invalid Data Type")
@Parcelize
data class FeedXProduct(
        @SerializedName("appLink")
        var appLink: String = "",
        @SerializedName("bebasOngkirStatus")
        var bebasOngkirStatus: String = "",
        @SerializedName("bebasOngkirURL")
        var bebasOngkirURL: String = "",
        @SerializedName("coverURL")
        var coverURL: String = "",
        @SerializedName("discount")
        var discount: Int = 0,
        @SerializedName("discountFmt")
        var discountFmt: String = "",
        @SerializedName("isCashback")
        var isCashback: Boolean = false,
        @SerializedName("cashbackFmt")
        var cashbackFmt: String = "",
        @SerializedName("id")
        var id: String = "",
        @SerializedName("isBebasOngkir")
        var isBebasOngkir: Boolean = false,
        @SerializedName("isDiscount")
        var isDiscount: Boolean = false,
        @SerializedName("mods")
        var mods: List<String> = emptyList(),
        @SerializedName("name")
        var name: String = "",
        @SerializedName("price")
        val price: Int = 0,
        @SerializedName("priceDiscount")
        var priceDiscount: Int = 0,
        @SerializedName("priceDiscountFmt")
        var priceDiscountFmt: String = "",
        @SerializedName("priceFmt")
        var priceFmt: String = "",
        @SerializedName("priceOriginal")
        var priceOriginal: Int = 0,
        @SerializedName("priceOriginalFmt")
        var priceOriginalFmt: String = "",
        @SerializedName("star")
        var star: Int = 0,
        @SerializedName("totalSold")
        var totalSold: Int = 0,
        @SerializedName("webLink")
        var webLink: String = "",
        //new fields for asgc fst and rs
        @SerializedName("priceMasked")
        var priceMasked: Float = 0f,
        @SerializedName("priceMaskedFmt")
        var priceMaskedFmt: String = "",
        @SerializedName("stockWording")
        val stockWording: String = "",
        @SerializedName("stockSoldPercentage")
        val stockSoldPercentage: Float = 0f,
        @SerializedName("cartable")
        val cartable: Boolean = false,

        //TopadsHeadline
        var variant: Int = 1,
        var isWishlisted :Boolean = false,
        var productName : String= "",
        val slashedPrice : String = "",
        val authorName : String = "",
        @SerializedName("shopID")
        var shopID : String = "",
        val isTopads: Boolean = false,
        val adClickUrl: String = "",
        val shopName : String = ""
        ):Parcelable
