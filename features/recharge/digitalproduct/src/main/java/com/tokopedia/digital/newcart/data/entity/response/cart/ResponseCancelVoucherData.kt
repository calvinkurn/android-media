package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCancelVoucherData(
        @SerializedName("type")
        @Expose
        var type: String? = "",
        @SerializedName("id")
        @Expose
        var id: String? = "",
        @SerializedName("attributes")
        @Expose
        var attributes: AttributesCancelVoucherData? = AttributesCancelVoucherData()
)

class AttributesCancelVoucherData(
        @SerializedName("success")
        @Expose
        var success: Boolean? = false
)