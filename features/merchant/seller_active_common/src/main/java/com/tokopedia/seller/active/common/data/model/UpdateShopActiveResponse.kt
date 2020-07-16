package com.tokopedia.seller.active.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateShopActiveResponse (
        @SerializedName("updateShopActive")
        @Expose
        val updateShopActive: UpdateShopActiveModel
)