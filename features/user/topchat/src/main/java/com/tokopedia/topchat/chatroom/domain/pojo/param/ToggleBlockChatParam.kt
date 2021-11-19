package com.tokopedia.topchat.chatroom.domain.pojo.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ToggleBlockChatParam(
    @SerializedName(PARAM_MSG_ID)
    var msgId: String = "",
    @SerializedName(PARAM_BLOCK_TYPE)
    var blockType: String = BlockType.Personal.value,
    @SerializedName(PARAM_IS_BLOCKED)
    var isBlocked: Boolean = false
): GqlParam {
    companion object {
        const val PARAM_MSG_ID = "messageID"
        const val PARAM_BLOCK_TYPE = "blockType"
        const val PARAM_IS_BLOCKED = "isBlocked"
    }
}

enum class BlockType(val value: String) {
    Personal("1"),
    Promo("2")
}