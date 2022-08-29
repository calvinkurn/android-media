package com.tokopedia.manageaddress.domain.model.shareaddress

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ValidateShareAddressAsReceiverParam(
    @SerializedName("sender_user_id")
    val senderUserId: String? = null,
    @SerializedName("source")
    val source: String? = null,
) : GqlParam