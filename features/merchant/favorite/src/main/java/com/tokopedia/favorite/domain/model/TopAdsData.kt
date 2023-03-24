package com.tokopedia.favorite.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TopAdsData(
    @SerializedName("data") @Expose var data: ShopItemData? = null,
    @SerializedName("message_error") @Expose var messageError: List<String>? = null
)

