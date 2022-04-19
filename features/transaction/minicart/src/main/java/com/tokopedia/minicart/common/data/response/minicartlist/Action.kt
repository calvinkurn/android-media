package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Action(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: String = ""
) {
    companion object {
        const val ACTION_WISHLIST = "1"
        const val ACTION_DELETE = "2"
        const val ACTION_NOTES = "3"
        const val ACTION_VALIDATENOTES = "4"
        const val ACTION_SIMILARPRODUCT = "5"
        const val ACTION_CHECKOUTBROWSER = "6"
        const val ACTION_SHOWLESS = "7"
        const val ACTION_SHOWMORE = "8"
        const val ACTION_VERIFICATION = "9"
        const val ACTION_WISHLISTED = "10"
        const val ACTION_FOLLOWSHOP = "11"
    }

}