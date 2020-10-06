package com.tokopedia.product.detail.data.model.restrictioninfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 06/10/20
 */
data class RestrictionInfoResponse(
        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("restrictionData")
        @Expose
        val restrictionData: List<RestrictionData> = listOf()
)

data class RestrictionData(
        @SerializedName("productID")
        @Expose
        val productId: String = "",

        @SerializedName("isEligible")
        @Expose
        val isEligible: Boolean = false,

        @SerializedName("action")
        @Expose
        val action: List<RestrictionAction> = listOf()
)

data class RestrictionAction(
        @SerializedName("actionType")
        @Expose
        val actionType: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("attributeName")
        @Expose
        val attributeName: String = ""
)