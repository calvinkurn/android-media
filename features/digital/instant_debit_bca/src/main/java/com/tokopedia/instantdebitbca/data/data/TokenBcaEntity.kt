package com.tokopedia.instantdebitbca.data.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 22/03/19.
 */
class TokenBcaEntity {

    @SerializedName("accessToken")
    @Expose
    val accessToken: String? = null
    @SerializedName("tokenType")
    @Expose
    val tokenType: String? = null
}
