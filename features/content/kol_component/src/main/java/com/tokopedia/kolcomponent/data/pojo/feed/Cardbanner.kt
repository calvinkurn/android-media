package com.tokopedia.kolcomponent.data.pojo.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Body
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.kolcomponent.data.pojo.feed.contentitem.Title

class Cardbanner {

    @SerializedName("title")
    @Expose
    val title: Title = Title()
}