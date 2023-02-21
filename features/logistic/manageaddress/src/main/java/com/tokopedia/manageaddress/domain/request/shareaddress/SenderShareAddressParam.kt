package com.tokopedia.manageaddress.domain.request.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SenderShareAddressParam(
    @SerializedName("param")
    val param: SenderShareAddressData = SenderShareAddressData(),
) : GqlParam {

    data class SenderShareAddressData(
        @SerializedName("source")
        val source: String = "",
        @SerializedName("sender_user_ids")
        val senderUserIds: List<String> = arrayListOf(),
    ) : GqlParam

}
