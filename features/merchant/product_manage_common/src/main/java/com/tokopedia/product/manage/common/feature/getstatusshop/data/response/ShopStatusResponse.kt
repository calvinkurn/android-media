package com.tokopedia.product.manage.common.feature.getstatusshop.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.ShopInfoById

data class ShopStatusResponse(
    @Expose
    @SerializedName("shopInfoByID")
    val shopInfoById: ShopInfoById
)
