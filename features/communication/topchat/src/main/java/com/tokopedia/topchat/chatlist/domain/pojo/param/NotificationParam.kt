package com.tokopedia.topchat.chatlist.domain.pojo.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class NotificationParam(
    @SerializedName("shop_id")
    val shopId: String
) : GqlParam
