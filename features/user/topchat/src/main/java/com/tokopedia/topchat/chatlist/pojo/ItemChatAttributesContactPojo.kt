package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatAttributesContactPojo constructor(
        @SerializedName("id")
        @Expose
        var contactId: String = "",
        @SerializedName("role")
        @Expose
        var role: String = "",
        @SerializedName("domain")
        @Expose
        var domain: String = "",
        @SerializedName("name")
        @Expose
        var contactName: String = "",
        @SerializedName("shopStatus")
        @Expose
        var shopStatus: Int,
        @SerializedName("tag")
        @Expose
        var tag: String = "",
        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String = "",
        @SerializedName("is_auto_reply")
        @Expose
        var isAutoReply: Boolean = false,
        @SerializedName("to_uid")
        @Expose
        var toUid: String = ""

)