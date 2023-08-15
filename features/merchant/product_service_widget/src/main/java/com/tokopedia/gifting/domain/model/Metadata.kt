package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.SerializedName

class Metadata {
    @SerializedName("Pictures")
    var pictures: List<Picture> = emptyList()

    @SerializedName("NotesTemplate")
    var notesTemplate: String = ""

    @SerializedName("Description")
    var description: String = ""

    @SerializedName("CustomInfoJSON")
    var customInfoJSON: String = ""

    @SerializedName("InfoURL")
    var infoURL: InfoURL = InfoURL()
}

class InfoURL {
    @SerializedName("IconURL")
    var iconURL: String = ""

    @SerializedName("IconDarkURL")
    var iconDarkURL: String = ""

    @SerializedName("EduPageURL")
    var eduPageURL: String = ""

    @SerializedName("AppLinkURL")
    var appLinkURL: String = ""

    @SerializedName("Title")
    var title: String = ""
}
