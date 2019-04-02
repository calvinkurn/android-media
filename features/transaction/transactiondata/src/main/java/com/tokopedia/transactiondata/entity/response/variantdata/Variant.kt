package com.tokopedia.transactiondata.entity.response.variantdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Variant(

        @SerializedName("product_variant_id")
        @Expose
        val productVariantId: Int,

        @SerializedName("variant_name")
        @Expose
        val variantName: String,

        @SerializedName("identifier")
        @Expose
        val identifier: String,

        @SerializedName("position")
        @Expose
        val position: Int,

        @SerializedName("options")
        @Expose
        val options: ArrayList<Option>

)