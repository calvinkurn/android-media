package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatAttributesContactPojo(
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
        var thumbnail: String = ""

)