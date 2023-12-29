package com.tokopedia.chat_common.data

import com.google.gson.annotations.SerializedName

class AttachmentVariantOption(
    @SerializedName("id") // ex:23454
    val id: String? = "-1",

    @SerializedName("value")
    val value: String? = "", // example: "White"

    @SerializedName("hex")
    val hex: String? = "" // ex: # ff3303
) {
    fun isColor(): Boolean {
        return !hex.isNullOrBlank()
    }
}
