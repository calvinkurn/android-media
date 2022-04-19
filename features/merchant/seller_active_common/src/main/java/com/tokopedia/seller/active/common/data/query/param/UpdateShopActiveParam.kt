package com.tokopedia.seller.active.common.data.query.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateShopActiveParam (
        @SerializedName("device")
        @Expose
        val device: String
)