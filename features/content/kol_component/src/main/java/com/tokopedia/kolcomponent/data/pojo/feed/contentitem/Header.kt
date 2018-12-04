package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Header {

    @SerializedName("avatar")
    @Expose
    var avatar: String? = ""
    @SerializedName("avatarTitle")
    @Expose
    var avatarTitle: String? = ""
    @SerializedName("avatarDate")
    @Expose
    var avatarDate: String? = ""
    @SerializedName("avatarWeblink")
    @Expose
    var avatarWeblink: String? = ""
    @SerializedName("avatarApplink")
    @Expose
    var avatarApplink: String? = ""
    @SerializedName("avatarDescription")
    @Expose
    var avatarDescription: String? = ""
    @SerializedName("avatarBadgeImage")
    @Expose
    var avatarBadgeImage: String? = ""
    @SerializedName("reportable")
    @Expose
    var isReportable: Boolean = false

}