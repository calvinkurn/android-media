package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class ProductImage(
        @SerializedName("image_src")
        @Expose
        val imageSrc: String = "",
        @SerializedName("image_src_200_square")
        @Expose
        val imageSrc200Square: String = "",
        @SerializedName("image_src_300")
        @Expose
        val imageSrc300: String = "",
        @SerializedName("image_src_square")
        @Expose
        val imageSrcSquare: String = ""
)
