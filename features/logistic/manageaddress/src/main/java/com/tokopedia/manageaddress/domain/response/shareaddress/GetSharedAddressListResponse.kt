package com.tokopedia.manageaddress.domain.response.shareaddress

import com.google.gson.annotations.SerializedName

data class GetSharedAddressListResponse(
        @SerializedName("KeroAddrGetSharedAddressList")
        var keroGetSharedAddressList: KeroAddrGetSharedAddressList? = null
)

data class KeroAddrGetSharedAddressList(
        @SerializedName("data")
        var data: List<SharedAddressData>? = null,
        @SerializedName("number_of_request")
        var numberOfRequest: Int? = null,
        @SerializedName("number_of_displayed")
        var numberOfDisplayed: Int? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("kero_addr_error")
        val error: KeroAddressError? = null
)

data class SharedAddressData(
        @SerializedName("sender_user_id")
        var senderUserId: Long? = 0L,
        @SerializedName("receiver_name")
        var receiverName: String = "",
        @SerializedName("addr_name")
        var addrName: String = "",
        @SerializedName("masked_address_1")
        var maskedAddress1: String = "",
        @SerializedName("masked_address_2")
        var maskedAddress2: String = "",
        @SerializedName("masked_phone")
        var maskedPhone: String = "",
        @SerializedName("masked_latitude")
        var maskedLatitude: String = "",
        @SerializedName("masked_longitude")
        var maskedLongitude: String = ""
)
