package com.tokopedia.libra.data.entity

import com.google.gson.annotations.SerializedName

data class LibraResponse(
    @SerializedName("getHomeLibraParameters") val experiments: List<LibraParameter>
)
