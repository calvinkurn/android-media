package com.tokopedia.common_wallet.balance.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AbTagEntity(
        @SerializedName("__typename")
        @Expose
        val typename: String = "",
        @SerializedName("tag")
        @Expose
        val tag: String = "")
