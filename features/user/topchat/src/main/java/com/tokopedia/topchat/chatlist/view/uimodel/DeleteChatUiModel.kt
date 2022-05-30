package com.tokopedia.topchat.chatlist.view.uimodel

import com.google.gson.annotations.SerializedName

/**
 * Created by stevenfredian on 10/30/17.
 */
data class DeleteChatUiModel (
    @SerializedName("msg_id")
    var msgId: String = "0",

    @SerializedName("is_success")
    var isSuccess: Int = 0,

    @SerializedName("detail_response")
    var detailResponse: String? = null
)