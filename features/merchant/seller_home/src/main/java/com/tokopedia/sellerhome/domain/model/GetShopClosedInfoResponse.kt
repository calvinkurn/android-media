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
    @SerializedName("shopCore")
    val coreInfo: ShopCoreInfoResponse? = ShopCoreInfoResponse(),
    @Expose
    @SerializedName("closedInfo")
    val closedInfo: ShopClosedInfoResponse = ShopClosedInfoResponse(),
    @Expose
    @SerializedName("statusInfo")
    val statusInfo: ShopStatusInfoResponse = ShopStatusInfoResponse(),
    @Expose
    @SerializedName("shopSnippetURL")
    val shopSnippetURL: String? = ""
)

data class ShopCoreInfoResponse(
    @Expose
    @SerializedName("url")
    val url: String? = ""
)

data class ShopClosedInfoResponse(
    @Expose
    @SerializedName("detail")
    val detail: ShopClosedInfoDetailResponse = ShopClosedInfoDetailResponse()
)

data class ShopStatusInfoResponse(
    @Expose
    @SerializedName("shopStatus")
    val shopStatus: Int = 0
)

data class ShopClosedInfoDetailResponse(
    @Expose
    @SerializedName("startDate")
    val startDate: String = "",
    @Expose
    @SerializedName("endDate")
    val endDate: String = "",
    @Expose
    @SerializedName("status")
    val status: Int = 0
) {
    companion object {
        const val SHOP_STATUS_CLOSED = 2
    }

    fun isOpen() = status != SHOP_STATUS_CLOSED
    fun isClosed() = status == SHOP_STATUS_CLOSED
}

data class GetShopClosedInfoError(
    @Expose
    @SerializedName("message")
    val message: String
)