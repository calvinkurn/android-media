package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoAttributesProduct(
        @SerializedName("desc")
        @Expose
        val desc: String,
        @SerializedName("detail")
        @Expose
        val detail: String,
        @SerializedName("detail_url")
        @Expose
        val detailUrl: String,
        @SerializedName("detail_url_text")
        @Expose
        val detailUrlText: String,
        @SerializedName("info")
        @Expose
        val info: String,
        @SerializedName("price")
        @Expose
        val price: String,
        @SerializedName("price_plain")
        @Expose
        val pricePlain: Int,
        @SerializedName("status")
        @Expose
        val status: Int,
        @SerializedName("detail_compact")
        @Expose
        val detailCompact: String
)