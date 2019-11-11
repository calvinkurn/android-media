package com.tokopedia.discovery.categoryrevamp.data.bannedCategory

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {

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

    @SerializedName("displayButton")
    var displayButton: Boolean = true


    @SerializedName("isBanned")
    var isBanned: Int = 0

    @SerializedName("isAdult")
    var isAdult: Int = 0

    override fun toString(): String {
        return "Data{" +
                "banned_message = '" + bannedMessage + '\''.toString() +
                ",app_redirection = '" + appRedirection + '\''.toString() +
                ",isBanned = '" + isBanned + '\''.toString() +
                ",isAdult = '" + isAdult + '\''.toString() +
                "}"
    }
}