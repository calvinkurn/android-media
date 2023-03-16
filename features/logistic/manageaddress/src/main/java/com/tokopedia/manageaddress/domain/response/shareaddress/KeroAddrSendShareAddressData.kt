package com.tokopedia.manageaddress.domain.response.shareaddress

import com.google.gson.annotations.SerializedName

data class KeroAddrSendShareAddressData(
    @SerializedName("number_of_request")
    var numberOfRequest: Int? = null,
    @SerializedName("kero_addr_error")
    var error: KeroAddressError? = null,
)
