package com.tokopedia.discovery.categoryrevamp.data.bannedCategory

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem

class Data {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("bannedMsg")
    var bannedMessage: String? = null


    @SerializedName("bannedMsgHeader")
    var bannedMsgHeader: String? = null

    @SerializedName("redirectionURL")
    var redirectionURL: String? = null

    @SerializedName("appRedirectionURL")
    var appRedirectionURL: String? = null

    @SerializedName("appRedirection")
    var appRedirection: String? = null

    @SerializedName("isBanned")
    var isBanned: Int = 0

    @SerializedName("isAdult")
    var isAdult: Int = 0

    @SerializedName("child")
    val child: List<SubCategoryItem?>? = null

    override fun toString(): String {
        return "Data{" +
                "banned_message = '" + bannedMessage + '\''.toString() +
                ",app_redirection = '" + appRedirection + '\''.toString() +
                ",redirectionURL = '" + redirectionURL + '\''.toString() +
                ",appRedirectionURL = '" + appRedirectionURL + '\''.toString() +
                ",isBanned = '" + isBanned + '\''.toString() +
                ",isAdult = '" + isAdult + '\''.toString() +
                "}"
    }
}