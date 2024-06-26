package com.tokopedia.localizationchooseaddress.domain.response

import com.google.gson.annotations.SerializedName

data class GetStateChosenAddressQglResponse (
        @SerializedName("keroAddrGetStateChosenAddress")
        var response: GetStateChosenAddressResponse = GetStateChosenAddressResponse()
)

data class GetStateChosenAddressResponse(
        @SerializedName("data")
        var data: ChosenAddressDataResponse = ChosenAddressDataResponse(),
        @SerializedName("tokonow")
        var tokonow: Tokonow = Tokonow(),
        @SerializedName("kero_addr_error")
        var error: ErrorChosenAddress = ErrorChosenAddress(),
        @SerializedName("status")
        var status: String = "",
        @SerializedName("server_process_time")
        var serverProcessTime: String = "",
        @SerializedName("config")
        var config: String = ""
)