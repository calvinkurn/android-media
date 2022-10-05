package com.tokopedia.product.addedit.preview.data.source.api.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ShippingLoc(
    @SuppressLint("Invalid Data Type") // provinceId currently using Integer at server
    @SerializedName("provinceID")
    val provinceId: Int
)
