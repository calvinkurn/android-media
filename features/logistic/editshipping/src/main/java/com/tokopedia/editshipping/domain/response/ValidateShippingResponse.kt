package com.tokopedia.editshipping.domain.response

import com.google.gson.annotations.SerializedName

data class ValidateShippingResponse(
        @SerializedName("kero_shipping_editor_mobile_popup")
        var response: KeroShippingEditorPopUp = KeroShippingEditorPopUp()
)

data class KeroShippingEditorPopUp(
        @SerializedName("config")
        var config: String = "",
        @SerializedName("server_process_time")
        var serverProcessTime: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("message_status")
        var messageStatus: String = "",
        @SerializedName ("data")
        var data: Data = Data()
)

data class Data(
        @SerializedName("show_popup")
        var showPopup: Boolean = false,
        @SerializedName("ticker_title")
        var tickerTitle: String = "",
        @SerializedName("ticker_content")
        var tickerContent: String = "",
        @SerializedName("popup_content")
        var popupContent: List<String> = listOf()
)
