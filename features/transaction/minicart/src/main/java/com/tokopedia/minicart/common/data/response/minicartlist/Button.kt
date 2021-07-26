package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Button(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("message")
        val message: String = ""
) {
    companion object {
        const val ID_START_SHOPPING = "1"
        const val ID_RETRY = "2"
        const val ID_HOMEPAGE = "3"
        const val ID_SETTING = "4"
    }
}