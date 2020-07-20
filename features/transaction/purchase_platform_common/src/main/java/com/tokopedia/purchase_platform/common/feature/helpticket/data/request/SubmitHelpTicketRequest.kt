package com.tokopedia.purchase_platform.common.feature.helpticket.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubmitHelpTicketRequest {

    @SerializedName("error_code")
    @Expose
    var errorCode: Int = 200

    @SerializedName("error_message")
    @Expose
    var errorMessage: String = ""

    @SerializedName("header_message")
    @Expose
    var headerMessage: String = ""

    @SerializedName("request_url")
    @Expose
    var requestUrl: String = ""

    @SerializedName("page")
    @Expose
    var page: String = ""

    @SerializedName("api_json_response")
    @Expose
    var apiJsonResponse: String = ""
}