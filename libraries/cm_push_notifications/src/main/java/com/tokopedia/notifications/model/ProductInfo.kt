package com.tokopedia.notifications.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductInfo(

        @SerializedName("productId")
        @ColumnInfo(name = "productId")
        @Expose
        var productId: Int?,

        @SerializedName("productTitle")
        @ColumnInfo(name = "productTitle")
        @Expose
        var productTitle: String,

        @SerializedName("productImg")
        @ColumnInfo(name = "productImg")
        @Expose
        var productImage: String,

        @SerializedName("actualPrice")
        @ColumnInfo(name = "actualPrice")
        @Expose
        var productActualPrice: String?,

        @SerializedName("currentPrice")
        @ColumnInfo(name = "currentPrice")
        @Expose
        var productCurrentPrice: String,

        @SerializedName("droppedPercent")
        @ColumnInfo(name = "droppedPercent")
        @Expose
        var productPriceDroppedPercentage: String?,

        @SerializedName("message")
        @ColumnInfo(name = "message")
        @Expose
        var productMessage: String,

        @SerializedName("stockMessage")
        @ColumnInfo(name = "stockMessage")
        @Expose
        var stockMessage: String? = "",

        @SerializedName("buttonTxt")
        @ColumnInfo(name = "buttonTxt")
        @Expose
        var productButtonMessage: String,

        @SerializedName("shopId")
        @ColumnInfo(name = "shopId")
        @Expose
        var shopId: Int?,

        @SerializedName("appLink")
        @ColumnInfo(name = "appLink")
        @Expose
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        @ColumnInfo(name = CMConstant.PayloadKeys.ELEMENT_ID)
        @Expose
        var element_id: String? = "",

        @SerializedName(CMConstant.PayloadKeys.FREE_DELIVERY)
        @ColumnInfo(name = CMConstant.PayloadKeys.FREE_DELIVERY)
        @Expose
        var freeOngkirIcon: String? = "",

        @SerializedName(CMConstant.PayloadKeys.REVIEW_ICON)
        @ColumnInfo(name = CMConstant.PayloadKeys.REVIEW_ICON)
        @Expose
        var reviewIcon: String? = "",

        @SerializedName(CMConstant.PayloadKeys.STOCK_AVAILABLE)
        @ColumnInfo(name = CMConstant.PayloadKeys.STOCK_AVAILABLE)
        @Expose
        var stockAvailable: String? = "",

        @SerializedName(CMConstant.PayloadKeys.REVIEW_SCORE)
        @ColumnInfo(name = CMConstant.PayloadKeys.REVIEW_SCORE)
        @Expose
        var reviewScore: Double? = 0.0,

        @SerializedName(CMConstant.PayloadKeys.REVIEW_NUMBER)
        @ColumnInfo(name = CMConstant.PayloadKeys.REVIEW_NUMBER)
        @Expose
        var reviewNumber: String? = "",

        @SerializedName(CMConstant.PayloadKeys.ACTION_BUTTON)
        @ColumnInfo(name = CMConstant.PayloadKeys.ACTION_BUTTON)
        var actionButton: ArrayList<ActionButton> = ArrayList()

) : Parcelable {

    fun getNumberPrice(): String {
        val startIndex = productCurrentPrice.indexOfFirst { it.isDigit() }
        val endIndex = productCurrentPrice.indexOfLast { it.isDigit() } + 1
        return productCurrentPrice
                .substring(startIndex, endIndex)
                .filter { it.isDigit() || it == '.' }
    }

}
