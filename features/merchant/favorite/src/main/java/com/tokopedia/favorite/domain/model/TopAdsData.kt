package com.tokopedia.favorite.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TopAdsData(
    @SerializedName("config") @Expose var config: String? = null,
    @SerializedName("status") @Expose var status: String? = null,
    @SerializedName("server_process_time") @Expose var serverProcessTime: String? = null,
    @SerializedName("data") @Expose var data: ShopItemData? = null,
    @SerializedName("message_error") @Expose var messageError: List<String>? = null
)

