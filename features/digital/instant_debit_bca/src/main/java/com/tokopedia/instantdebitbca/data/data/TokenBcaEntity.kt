package com.tokopedia.instantdebitbca.data.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 22/03/19.
 */
data class TokenBcaEntity (

    @SerializedName("accessToken")
    @Expose
    val accessToken: String? = "",
    @SerializedName("tokenType")
    @Expose
    val tokenType: String? = ""
)
