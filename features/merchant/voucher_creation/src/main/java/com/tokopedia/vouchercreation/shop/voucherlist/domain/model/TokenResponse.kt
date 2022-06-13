package com.tokopedia.vouchercreation.shop.voucherlist.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class TokenResponse(
        @SerializedName("getInitiateVoucherPage")
        val tokenModel: TokenModel = TokenModel()
)

data class TokenModel(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val data: TokenData = TokenData()
)

data class TokenData(
        @SerializedName("token")
        val token: String = ""
)