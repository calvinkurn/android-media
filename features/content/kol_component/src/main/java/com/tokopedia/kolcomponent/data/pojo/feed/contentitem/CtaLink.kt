package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

class CtaLink {
    /**
     * text :
     * appLink :
     * webLink :
     */

    @SerializedName("text")
    var text: String? = null
    @SerializedName("appLink")
    var appLink: String? = null
    @SerializedName("webLink")
    var webLink: String? = null
}