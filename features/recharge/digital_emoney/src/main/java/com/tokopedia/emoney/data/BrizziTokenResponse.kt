package com.tokopedia.emoney.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BrizziTokenResponse(
        @SerializedName("rechargeEmoneyBRIToken")
        @Expose
        val tokenResponse: BrizziToken
)

class BrizziToken(
        @SerializedName("token")
        @Expose
        val token: String = ""
)