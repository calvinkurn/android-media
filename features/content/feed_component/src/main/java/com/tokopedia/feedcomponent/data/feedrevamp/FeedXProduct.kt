package com.tokopedia.feedcomponent.data.feedrevamp


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
        var id: String = "",
        @SerializedName("isBebasOngkir")
        var isBebasOngkir: Boolean = false,
        @SerializedName("isDiscount")
        var isDiscount: Boolean = false,
        @SerializedName("mods")
        var mods: List<String> = emptyList(),
        @SerializedName("name")
        var name: String = "",
        @SuppressLint("Invalid Data Type") @SerializedName("price")
        var price: Int = 0,
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

        //TopadsHeadline
        var variant: Int = 1,
        var isWishlisted :Boolean = false,
        var productName : String= "",
        val slashedPrice : String = "",
        val authorName : String = "",
        var shopID : String = "",
        val isTopads: Boolean = false,
        val adClickUrl: String = "",
        val shopName : String = ""
        ):Parcelable