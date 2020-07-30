package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Lock(
        @SerializedName("full")
        val full: Boolean = false,
        @SerializedName("partial")
        val partial: Partial = Partial()
)

data class Partial(
        @SerializedName("price")
        val price: Boolean = false,
        @SerializedName("status")
        val status: Boolean = false,
        @SerializedName("stock")
        val stock: Boolean = false,
        @SerializedName("wholesale")
        val wholesale: Boolean = false,
        @SerializedName("name")
        val name: Boolean = false
)