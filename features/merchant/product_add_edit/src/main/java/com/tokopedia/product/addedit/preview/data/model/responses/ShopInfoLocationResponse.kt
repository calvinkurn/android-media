package com.tokopedia.product.addedit.preview.data.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.addedit.preview.data.source.api.response.ShopInfoById

data class ShopInfoLocationResponse(
        @Expose
        @SerializedName("shopInfoByID")
        val shopInfoById: ShopInfoById
)