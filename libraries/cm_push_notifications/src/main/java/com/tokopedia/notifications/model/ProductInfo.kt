package com.tokopedia.notifications.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductInfo(

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

        @SerializedName("buttonTxt")
        @ColumnInfo(name = "buttonTxt")
        @Expose
        var productButtonMessage: String,

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

        @SerializedName(CMConstant.PayloadKeys.STOCK_AVAILABLE)
        @ColumnInfo(name = CMConstant.PayloadKeys.STOCK_AVAILABLE)
        @Expose
        var stockAvailable: String? = "",

        @SerializedName(CMConstant.PayloadKeys.REVIEW_SCORE)
        @ColumnInfo(name = CMConstant.PayloadKeys.REVIEW_SCORE)
        @Expose
        var reviewScore: String? = "",

        @SerializedName(CMConstant.PayloadKeys.ACTION_BUTTON)
        @ColumnInfo(name = CMConstant.PayloadKeys.ACTION_BUTTON)
        var actionButton: ArrayList<ActionButton> = ArrayList()

) : Parcelable
