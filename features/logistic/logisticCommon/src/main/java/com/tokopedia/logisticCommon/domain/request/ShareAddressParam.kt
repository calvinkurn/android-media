package com.tokopedia.logisticCommon.domain.request

import com.google.gson.annotations.SerializedName

data class ShareAddressParam(
    @SerializedName("sender_user_id")
    val senderUserId: String,
    @SerializedName("sender_address_id")
    val senderAddressId: String,
    @SerializedName("receiver_phone_number_or_email")
    val receiverPhoneNumberOrEmail: String,
    @SerializedName("initial_check")
    val initialCheck: Boolean
)