package com.tokopedia.product.detail.data.model.upcoming

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef

/**
 * Created by Yehezkiel on 21/07/20
 */
data class ProductUpcomingData(
        @SerializedName("campaignID")
        @Expose
        val campaignId: String? = "",

        @SerializedName("campaignType")
        @Expose
        val campaignType: String? = "",

        @SerializedName("campaignTypeName")
        @Expose
        val campaignTypeName: String? = "",

        @SerializedName("startDate")
        @Expose
        val startDate: String? = "",

        @SerializedName("endDate")
        @Expose
        val endDate: String? = "",

        @SerializedName("notifyMe")
        @Expose
        var notifyMe: Boolean? = false,

        @SerializedName("productID")
        @Expose
        val productId: String? = "",

        @SerializedName("ribbonCopy")
        @Expose
        val ribbonCopy: String? = "",

        @SerializedName("upcomingType")
        @Expose
        val upcomingType: String? = ""
) {
        fun isUpcomingNplType(): Boolean = upcomingType == ProductUpcomingTypeDef.UPCOMING_NPL
}