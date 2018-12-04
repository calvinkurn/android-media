package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Caption
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Media

class Body {

    @SerializedName("caption")
    @Expose
    var caption: Caption? = null
    @SerializedName("media")
    @Expose
    var media: List<Media>? = null

}