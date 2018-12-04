package com.tokopedia.kolcomponent.data.pojo.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Body
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Title

class Cardpost {
    @SerializedName("title")
    @Expose
    val title: Title = Title()

    @SerializedName("header")
    @Expose
    val header: Header = Header()

    @SerializedName("body")
    @Expose
    val body: Body = Body()

    @SerializedName("footer")
    @Expose
    val footer: Footer = Footer()

}