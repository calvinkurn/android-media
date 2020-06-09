package com.tokopedia.one.click.checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class ProductShowCase(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("id")
    @Expose
    val id: Int = 0
)
