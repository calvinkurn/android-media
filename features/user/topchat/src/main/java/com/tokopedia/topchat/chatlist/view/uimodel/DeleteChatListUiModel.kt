package com.tokopedia.topchat.chatlist.view.uimodel

import com.google.gson.annotations.SerializedName

/**
 * Created by stevenfredian on 10/30/17.
 */
class DeleteChatListUiModel {
    @SerializedName("list")
    var list: List<DeleteChatUiModel>? = null
}