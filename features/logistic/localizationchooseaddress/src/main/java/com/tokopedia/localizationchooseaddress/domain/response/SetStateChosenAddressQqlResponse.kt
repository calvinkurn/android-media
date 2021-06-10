package com.tokopedia.localizationchooseaddress.domain.response

import com.google.gson.annotations.SerializedName

data class SetStateChosenAddressQqlResponse (
    @SerializedName("keroAddrSetStateChosenAddress")
    var response: SetStateChosenAddressResponse = SetStateChosenAddressResponse()
)

data class SetStateChosenAddressResponse(
            @SerializedName("data")
            var data: DataSetStateChooseAddressResponse = DataSetStateChooseAddressResponse(),
            @SerializedName("status")
            var status: String = "",
            @SerializedName("server_process_time")
            var serverProcessTime: String = "",
            @SerializedName("config")
            var config: String = ""
)

data class DataSetStateChooseAddressResponse(
        @SerializedName("is_success")
        var isSuccess: Int = 0,
        @SerializedName("chosen_address")
        var chosenAddressData:  ChosenAddressDataResponse = ChosenAddressDataResponse()
)