package com.tokopedia.product.addedit.preview.data.source.api.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

@SuppressLint("Invalid Data Type") 
data class Wholesale(
        @SerializedName("minQty")
        val minQty: Int = 0,
        @SerializedName("price")
        val price: String = ""
)