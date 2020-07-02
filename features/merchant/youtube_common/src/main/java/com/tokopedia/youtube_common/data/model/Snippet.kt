package com.tokopedia.youtube_common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Snippet (

    @SerializedName("publishedAt")
    @Expose
    var publishedAt: String? = null,
    @SerializedName("channelId")
    @Expose
    var channelId: String? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("thumbnails")
    @Expose
    var thumbnails: Thumbnails? = null,
    @SerializedName("channelTitle")
    @Expose
    var channelTitle: String? = null,
    @SerializedName("tags")
    @Expose
    var tags: List<String>? = null,
    @SerializedName("categoryId")
    @Expose
    var categoryId: String? = null,
    @SerializedName("liveBroadcastContent")
    @Expose
    var liveBroadcastContent: String? = null,
    @SerializedName("defaultLanguage")
    @Expose
    var defaultLanguage: String? = null,
    @SerializedName("localized")
    @Expose
    var localized: Localized? = null,
    @SerializedName("defaultAudioLanguage")
    @Expose
    var defaultAudioLanguage: String? = null
)
