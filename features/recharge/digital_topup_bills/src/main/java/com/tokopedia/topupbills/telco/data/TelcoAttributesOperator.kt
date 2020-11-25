package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
data class TelcoAttributesOperator(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("default_product_id")
        @Expose
        val defaultProductId: Int = 0
)