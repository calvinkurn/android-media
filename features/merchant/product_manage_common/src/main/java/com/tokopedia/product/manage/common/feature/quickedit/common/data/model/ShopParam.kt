package com.tokopedia.product.manage.common.feature.quickedit.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopParam(
        @SerializedName("id")
        @Expose
        var shopId: String = ""
)