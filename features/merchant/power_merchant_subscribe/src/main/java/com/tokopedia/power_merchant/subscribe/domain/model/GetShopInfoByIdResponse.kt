package com.tokopedia.power_merchant.subscribe.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 22/05/21
 */

data class GetShopInfoByIdResponse(
        @SerializedName("shopInfoByID")
        val shopInfoById: ShopInfoByIdModel = ShopInfoByIdModel()
)

data class ShopInfoByIdModel(
        @SerializedName("result")
        val result: List<ShopInfoResultModel> = emptyList()
)

data class ShopInfoResultModel(
        @SerializedName("statusInfo")
        val statusInfo: StatusInfoModel
)

data class StatusInfoModel(
        @SerializedName("shopStatus")
        val shopStatus: Int
)