package com.tokopedia.loginregister.shopcreation.data

import com.google.gson.annotations.SerializedName

data class GetShopInfoResponse(
    @SerializedName("userShopInfo")
    val shopInfo: ReserveStatusInfoData
)

data class ReserveStatusInfoData(
    @SerializedName("reserveStatusInfo")
    val reserveStatusInfo: StatusData
)

data class StatusData(
    @SerializedName("shopID")
    val shopId: Long,
    @SerializedName("shopName")
    val shopName: String,
    @SerializedName("domain")
    val domain: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("reasonID")
    val reasonId: Long
) {
    fun isShopPending(): Boolean = shopId > 0 && status == 0 && reasonId == 2L
}

sealed class ShopStatus {
    object Pending : ShopStatus()
    object NotRegistered: ShopStatus()

    data class Error(val throwable: Throwable): ShopStatus()
}
