package com.tokopedia.logisticCommon.data.response.shippingeditor

import com.google.gson.annotations.SerializedName

data class SaveShippingEditorResponse(
        @SerializedName("ShopLocSetStatus")
        var saveShippingEditor: SaveShippingResponse = SaveShippingResponse()
)

data class SaveShippingResponse(
        @SerializedName("status_message")
        val statusMessage: String = "",
        @SerializedName("is_success")
        val isSuccess: Boolean = false
)