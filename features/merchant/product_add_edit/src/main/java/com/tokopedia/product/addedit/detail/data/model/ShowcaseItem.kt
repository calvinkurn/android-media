package com.tokopedia.product.addedit.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShowcaseItem(
        @SerializedName("aceDefaultSort")
        @Expose
        var aceDefaultSort: Int = 0,
        @SerializedName("alias")
        @Expose
        var alias: String = "",
        @SerializedName("badge")
        @Expose
        var badge: String = "",
        @SerializedName("count")
        @Expose
        var count: Int = 0,
        @SerializedName("highlighted")
        @Expose
        var highlighted: Boolean = false,
        @SerializedName("id")
        var id: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("uri")
        var uri: String = "",
        @SerializedName("useAce")
        var useAce: Boolean = false
)