package com.tokopedia.discovery.categoryrevamp.data.bannedCategory

import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("bannedMsg")
    var bannedMessage: String? = null


    @SerializedName("bannedMsgHeader")
    var bannedMsgHeader: String? = null

    @SerializedName("appRedirection")
    var appRedirection: String? = null

    @SerializedName("isBanned")
    var isBanned: Int = 0

    @SerializedName("is_adult")
    var isAdult: Int = 0

    override fun toString(): String {
        return "Data{" +
                "banned_message = '" + bannedMessage + '\''.toString() +
                ",app_redirection = '" + appRedirection + '\''.toString() +
                ",is_banned = '" + isBanned + '\''.toString() +
                ",is_adult = '" + isAdult + '\''.toString() +
                "}"
    }
}