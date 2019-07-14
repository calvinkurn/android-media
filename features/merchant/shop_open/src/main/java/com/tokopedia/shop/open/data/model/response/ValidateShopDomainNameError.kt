package com.tokopedia.shop.open.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ValidateShopDomainNameError(
        @SerializedName("message")
        @Expose
        val message:String = ""
)