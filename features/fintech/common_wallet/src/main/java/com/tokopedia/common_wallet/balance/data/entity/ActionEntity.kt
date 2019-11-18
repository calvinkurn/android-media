package com.tokopedia.common_wallet.balance.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActionEntity(
        @SerializedName("__typename")
        @Expose
        val typename: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("redirect_url")
        @Expose
        val redirectUrl: String = "",
        @SerializedName("applinks")
        @Expose
        val applinks: String = "",
        @SerializedName("visibility")
        @Expose
        val visibility: String = "")
