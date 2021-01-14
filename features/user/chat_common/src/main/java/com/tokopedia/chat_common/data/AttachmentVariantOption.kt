package com.tokopedia.chat_common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttachmentVariantOption(
        @SerializedName("id") // ex:23454
        @Expose
        val id: Long = -1L,

        @SerializedName("value")
        @Expose
        val value: String = "", // example: "White"

        @SerializedName("hex")
        @Expose
        val hex: String = ""// ex:#ff3303
) {
    fun isColor(): Boolean {
        return hex.isNotEmpty()
    }
}