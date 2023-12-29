package com.tokopedia.manageaddress.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.response.KeroAddrStateChosenAddressData
import com.tokopedia.logisticCommon.data.response.KeroAddressRespTokonow

data class DeletePeopleAddressGqlResponse(
    @SerializedName("kero_remove_address")
    val response: DeletePeopleAddressResponse = DeletePeopleAddressResponse()
)

data class DeletePeopleAddressResponse(
    @SerializedName("data")
    val data: DeletePeopleAddressData = DeletePeopleAddressData(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("config")
    val config: String = "",
    @SerializedName("server_process_time")
    val serverTime: String = ""
)

data class DeletePeopleAddressData(
    @SerializedName("is_success")
    val success: Int = 0,
    @SerializedName("is_state_chosen_address_changed")
    val isStateChosenAddressChanged: Boolean = false,
    @SerializedName("chosen_address")
    var chosenAddressData: KeroAddrStateChosenAddressData = KeroAddrStateChosenAddressData(),
    @SerializedName("tokonow")
    var tokonow: KeroAddressRespTokonow = KeroAddressRespTokonow()
)
