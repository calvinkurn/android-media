package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TelcoRecommendation(
        @SerializedName("iconUrl")
        @Expose
        val iconUrl: String,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("clientNumber")
        @Expose
        val clientNumber: String,
        @SerializedName("appLink")
        @Expose
        val applink: String,
        @SerializedName("webLink")
        @Expose
        val weblink: String,
        @SerializedName("type")
        @Expose
        val type: String,
        @SerializedName("categoryId")
        @Expose
        val categoryId: Int,
        @SerializedName("productId")
        @Expose
        val productId: Int,
        @SerializedName("isATC")
        @Expose
        val isAtc: Boolean = false,
        @SerializedName("operatorID")
        @Expose
        val operatorId: Int
)