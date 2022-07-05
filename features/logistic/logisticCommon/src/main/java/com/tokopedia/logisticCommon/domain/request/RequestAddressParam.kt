package com.tokopedia.logisticCommon.domain.request

import com.google.gson.annotations.SerializedName

data class RequestAddressParam(
    @SerializedName("receiver_user_id")
    var receiverUserId: String,
    @SerializedName("sender_phone_number_or_email")
    var senderPhoneNumberOrEmail: String
)
