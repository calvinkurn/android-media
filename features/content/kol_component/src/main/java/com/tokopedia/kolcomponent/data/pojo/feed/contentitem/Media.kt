package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Media {
    @SerializedName("id")
    @Expose
    var id: String? = ""
    @SerializedName("type")
    @Expose
    var type: String? = ""
    @SerializedName("appLink")
    @Expose
    var appLink: String? = ""
    @SerializedName("webLink")
    @Expose
    var webLink: String? = ""
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = ""
    @SerializedName("ctaLink")
    @Expose
    var ctaLink: CtaLink? = CtaLink()
    @SerializedName("totalItems")
    @Expose
    var totalItems: Int = 0
    @SerializedName("isSelected")
    @Expose
    var isIsSelected: Boolean = false
    @SerializedName("mediaItems")
    @Expose
    var mediaItems: List<MediaItem>? = ArrayList()
}