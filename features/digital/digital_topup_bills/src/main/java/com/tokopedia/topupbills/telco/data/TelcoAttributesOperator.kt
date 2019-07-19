package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoAttributesOperator(
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("image")
        @Expose
        val image: String,
        @SerializedName("image_url")
        @Expose
        val imageUrl: String,
        @SerializedName("lastorder_url")
        @Expose
        val lastOrderUrl: String,
        @SerializedName("default_product_id")
        @Expose
        val defaultProductId: Int,
        @SerializedName("ussd")
        @Expose
        val ussd: String,
        @SerializedName("description")
        @Expose
        val description: String
)