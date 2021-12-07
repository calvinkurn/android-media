package com.tokopedia.topchat.chatroom.domain.pojo.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ChatAttachmentParam (
    @SerializedName(PARAM_MSG_ID)
    var msgId: Long = 0,

    @SerializedName(PARAM_REPLY_IDS)
    var replyIDs: String = "",

    @SerializedName(PARAM_ADDRESS_ID)
    var addressId: Long = 0,

    @SerializedName(PARAM_DISTRICT_ID)
    var districtId: Long = 0,

    @SerializedName(PARAM_POSTAL_CODE)
    var postalCode: String = "",

    @SerializedName(PARAM_LAT_LON)
    var latlon: String = ""
): GqlParam {

    companion object {
        const val PARAM_MSG_ID = "msgId"
        const val PARAM_REPLY_IDS = "ReplyIDs"
        const val PARAM_ADDRESS_ID = "addressID"
        const val PARAM_DISTRICT_ID = "districtID"
        const val PARAM_POSTAL_CODE = "postalCode"
        const val PARAM_LAT_LON = "latlon"
    }
}