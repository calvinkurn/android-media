package com.tokopedia.shop.common.graphql.data.isshopofficial

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir

data class GetIsShopOfficialStore(
        @SerializedName("data")
        @Expose
        val data: Data = Data(),
        @SerializedName("message_error")
        @Expose
        val messageError: String = ""
) {

    data class Response(
            @SerializedName("getIsOfficial")
            val result: GetIsShopOfficialStore = GetIsShopOfficialStore()
    )

    data class Data(
            @SerializedName("is_official")
            @Expose
            val isOfficial: Boolean = false,
            @SerializedName("expired_date")
            @Expose
            val expiredDate: String = ""
    )
}