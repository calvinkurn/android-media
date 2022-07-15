package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 25/04/22.
 */

data class GetShopInfoTickerResponse(
    @Expose
    @SerializedName("shopInfoByID")
    val shopInfo: ShopInfoByIdModel = ShopInfoByIdModel()
)

data class ShopInfoByIdModel(
    @Expose
    @SerializedName("result")
    val result: List<ShopInfoResultModel> = listOf()
)

data class ShopInfoResultModel(
    @Expose
    @SerializedName("statusInfo")
    val statusInfo: StatusInfoTickerModel = StatusInfoTickerModel()
)

data class StatusInfoTickerModel(
    @Expose
    @SerializedName("shopStatus")
    val shopStatus: Int = 0,
    @Expose
    @SerializedName("statusTitle")
    val title: String = "",
    @Expose
    @SerializedName("statusMessage")
    val message: String = "",
    @Expose
    @SerializedName("tickerType")
    val tickerType: String = ""
)