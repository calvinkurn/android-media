package com.tokopedia.feedcomponent.data.pojo.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Item
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.track.Tracking

data class CardRecommendation (

    @SerializedName("title")
    @Expose
    val title: Title = Title(),

    @SerializedName("items")
    @Expose
    val items: MutableList<Item> = ArrayList(),

    @SerializedName("tracking")
    @Expose
    val tracking: List<Tracking> = ArrayList()

)