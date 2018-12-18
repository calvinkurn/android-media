package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class MediaItem (
    @SerializedName("id")
    @Expose
    var id:String = "",

    @SerializedName("text")
    @Expose
    var text:String = "",

    @SerializedName("price")
    @Expose
    var price:String = "",

    @SerializedName("type")
    @Expose
    var type:String = "",

    @SerializedName("applink")
    @Expose
    var applink:String = "",

    @SerializedName("weblink")
    @Expose
    var weblink:String = "",

    @SerializedName("thumbnail")
    @Expose
    var thumbnail:String = "",

    @SerializedName("position")
    @Expose
    var position:List<Float> = ArrayList()
)