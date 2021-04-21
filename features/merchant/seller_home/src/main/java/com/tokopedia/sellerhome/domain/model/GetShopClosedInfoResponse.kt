package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopClosedInfoResponse(
    @Expose
    @SerializedName("shopInfoByID")
    val data: ShopInfoByIDResponse
)

data class ShopInfoByIDResponse(
    @Expose
    @SerializedName("result")
    val result: List<ShopInfoResultResponse>,
    @Expose
    @SerializedName("error")
    val error: GetShopClosedInfoError?
)

data class ShopInfoResultResponse(
    @Expose
    @SerializedName("closedInfo")
    val closedInfo: ShopClosedInfoResponse
)

data class ShopClosedInfoResponse(
    @Expose
    @SerializedName("detail")
    val detail: ShopClosedInfoDetailResponse
)

data class ShopClosedInfoDetailResponse(
    @Expose
    @SerializedName("startDate")
    val startDate: String,
    @Expose
    @SerializedName("openDate")
    val openDate: String
) {
    companion object {
        private const val START_DATE_SHOP_OPEN = "0"
    }

    fun isOpen() = startDate == START_DATE_SHOP_OPEN
    fun isClosed() = startDate != START_DATE_SHOP_OPEN
}

data class GetShopClosedInfoError(
    @Expose
    @SerializedName("message")
    val message: String
)