package com.tokopedia.manageaddress.domain.request.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SendShareAddressRequestParam(
    @SerializedName("param")
    var param: SendShareAddressRequestData = SendShareAddressRequestData()
) : GqlParam {

    data class SendShareAddressRequestData(
        @SerializedName("source")
        var source: String = "",
        @SerializedName("sender_phone_number_or_email")
        var senderPhoneNumberOrEmail: String = ""
    ) : GqlParam
}
