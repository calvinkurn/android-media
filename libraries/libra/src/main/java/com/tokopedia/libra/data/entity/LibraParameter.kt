package com.tokopedia.libra.data.entity

import com.google.gson.annotations.SerializedName

data class LibraParameter(
    @SerializedName("experiment") val experiment: String,
    @SerializedName("variant") val variant: String,
)
