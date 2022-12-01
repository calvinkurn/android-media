package com.tokopedia.manageaddress.domain.request.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ValidateShareAddressAsSenderParam(
    @SerializedName("receiver_user_id")
    val receiverUserId: String? = null,
    @SerializedName("source")
    val source: String? = null,
) : GqlParam
