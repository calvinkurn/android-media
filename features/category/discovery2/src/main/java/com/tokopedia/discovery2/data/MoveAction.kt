package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class MoveAction(

    @SerializedName("type")
    var type: String? = "",

    @SerializedName("value")
    var value: String? = ""

)
