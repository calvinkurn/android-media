package com.tokopedia.notifications.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductInfo(

        @SerializedName("productId")
        @Expose
        var productId: Long? = 0,

        @SerializedName("productTitle")
        @Expose
        var productTitle: String = "",

        @SerializedName("productImg")
        @Expose
        var productImage: String = "",

        @SerializedName("actualPrice")
        @Expose
        var productActualPrice: String? = "",

        @SerializedName("currentPrice")
        @Expose
        var productCurrentPrice: String = "",

        @SerializedName("droppedPercent")
        @Expose
        var productPriceDroppedPercentage: String? = "",

        @SerializedName("message")
        @Expose
        var productMessage: String = "",

        @SerializedName("stockMessage")
        @Expose
        var stockMessage: String? = "",

        @SerializedName("buttonTxt")
        @Expose
        var productButtonMessage: String = "",

        @SerializedName("shopId")
        @Expose
        var shopId: Long? = 0,

        @SerializedName("appLink")
        @Expose
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        @Expose
        var element_id: String? = "",

        @SerializedName(CMConstant.PayloadKeys.FREE_DELIVERY)
        @Expose
        var freeOngkirIcon: String? = "",

        @SerializedName(CMConstant.PayloadKeys.REVIEW_ICON)
        @Expose
        var reviewIcon: String? = "",

        @SerializedName(CMConstant.PayloadKeys.STOCK_AVAILABLE)
        @Expose
        var stockAvailable: String? = "",

        @SerializedName(CMConstant.PayloadKeys.REVIEW_SCORE)
        @Expose
        var reviewScore: Double? = 0.0,

        @SerializedName(CMConstant.PayloadKeys.REVIEW_NUMBER)
        @Expose
        var reviewNumber: String? = "",

        @SerializedName("customButtons")
        @Expose
        var productButtons: ArrayList<ActionButton> = ArrayList()

) : Parcelable {

    fun getNumberPrice(): String {
        val startIndex = productCurrentPrice.indexOfFirst { it.isDigit() }
        val endIndex = productCurrentPrice.indexOfLast { it.isDigit() } + 1
        return productCurrentPrice
                .substring(startIndex, endIndex)
                .filter { it.isDigit() || it == '.' }
    }

}
