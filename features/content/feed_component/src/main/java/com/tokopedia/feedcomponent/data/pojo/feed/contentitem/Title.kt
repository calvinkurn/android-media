package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Title (

    @SerializedName("text")
    @Expose
    var text: String = "",
    @SerializedName("textBadge")
    @Expose
    var textBadge: String = "",
    @SerializedName("isClicked")
    @Expose
    var isIsClicked: Boolean = false,
    @SerializedName("ctaLink")
    @Expose
    var ctaLink: CtaLink = CtaLink(),
    @SerializedName("action")
    var action: Action = Action()

)