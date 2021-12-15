package com.tokopedia.topchat.chatroom.domain.pojo.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class StickerParam (
    @SerializedName(PARAM_GROUP_UUID)
    var stickerGroupUID: String,

    @SerializedName(PARAM_LIMIT)
    var limit: Int = DEFAULT_PARAM_LIMIT
): GqlParam {
    companion object {
        const val PARAM_GROUP_UUID = "groupUUID"
        const val PARAM_LIMIT = "limit"
        private const val DEFAULT_PARAM_LIMIT = 16
    }
}