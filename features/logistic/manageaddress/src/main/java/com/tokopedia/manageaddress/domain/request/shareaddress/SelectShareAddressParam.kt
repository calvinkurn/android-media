package com.tokopedia.manageaddress.domain.request.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SelectShareAddressParam(
    @SerializedName("param")
    val param: Param = Param()
) : GqlParam {

    data class Param(
        @SerializedName("source")
        val source: String = "",
        @SerializedName("receiver_user_id")
        val receiverUserId: String = "",
        @SerializedName("sender_address_id")
        val senderAddressId: String = "",
        @SerializedName("approve")
        val approve: Boolean = false
    ) : GqlParam
}
