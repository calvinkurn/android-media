package com.tokopedia.vouchercreation.voucherlist.domain.model

import com.google.gson.annotations.SerializedName

data class ShopBasicDataResponse(
        @SerializedName("shopBasicData")
        val shopBasicData: ShopBasicData = ShopBasicData()
)

data class ShopBasicData(
        @SerializedName("result")
        val result: ShopBasicDataResult = ShopBasicDataResult(),
        @SerializedName("error")
        val error: ShopBasicDataError = ShopBasicDataError()
)

data class ShopBasicDataResult(
        @SerializedName("domain")
        val shopDomain: String = "",
        @SerializedName("name")
        val shopName: String = ""
)

data class ShopBasicDataError(
        @SerializedName("message")
        val errorMessage: String = ""
)