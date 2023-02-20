package com.tokopedia.manageaddress.domain.request.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ShareAddressToUserParam(
    @SerializedName("param")
    val param: ShareAddressToUserData = ShareAddressToUserData(),
) : GqlParam {

    data class ShareAddressToUserData(
        @SerializedName("source")
        val source: String = "",
        @SerializedName("sender_address_id")
        val senderAddressId: String = "",
        @SerializedName("receiver_phone_number_or_email")
        val receiverPhoneNumberOrEmail: String = "",
        @SerializedName("initial_check")
        val initialCheck: Boolean = false
    ) : GqlParam
}
