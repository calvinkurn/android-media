package com.tokopedia.product.addedit.description.presentation.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class YoutubeResponse {
    @SerializedName("kind")
    @Expose
    var kind: String? = null
    @SerializedName("etag")
    @Expose
    var etag: String? = null
    @SerializedName("pageInfo")
    @Expose
    var pageInfo: PageInfo? = null
    @SerializedName("items")
    @Expose
    var items: List<Item>? = null

}