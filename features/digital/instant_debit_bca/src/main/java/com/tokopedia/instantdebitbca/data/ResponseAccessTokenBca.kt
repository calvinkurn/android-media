package com.tokopedia.instantdebitbca.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 22/03/19.
 */
class ResponseAccessTokenBca(
    @SerializedName("token")
    @Expose
    val tokenBca: TokenEntity)
