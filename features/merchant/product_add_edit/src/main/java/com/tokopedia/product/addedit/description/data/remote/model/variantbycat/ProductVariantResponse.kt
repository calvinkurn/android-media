package com.tokopedia.product.addedit.description.data.remote.model.variantbycat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by faisalramd on 2020-03-27.
 */

data class ProductVariantResponse(
        @SerializedName("data")
        @Expose
        val data: List<ProductVariantByCatModel> = emptyList(),

        @SerializedName("status")
        @Expose
        val meta: String = ""
)