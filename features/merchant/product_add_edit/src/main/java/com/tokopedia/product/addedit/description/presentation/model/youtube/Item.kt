package com.tokopedia.product.addedit.description.presentation.model.youtube

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Item(
        @SerializedName("kind")
        @Expose
        var kind: String? = null,
        @SerializedName("etag")
        @Expose
        var etag: String? = null,
        @SerializedName("id")
        @Expose
        var id: String? = null,
        @SerializedName("snippet")
        @Expose
        var snippet: Snippet? = null,
        @SerializedName("contentDetails")
        @Expose
        var contentDetails: ContentDetails? = null
)