package com.tokopedia.createpost.common.domain.entity.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContentSubmitInput(
        @SerializedName("type")
        @Expose
        var type: String? = null,

        @SerializedName("action")
        @Expose
        var action: String? = null,

        @SerializedName("authorType")
        @Expose
        var authorType: String? = null,

        @SerializedName("authorID")
        @Expose
        var authorID: String? = null,

        @SerializedName("caption")
        @Expose
        var caption: String? = null,

        @SerializedName("adID")
        @Expose
        var adID: String? = null,

        @SerializedName("productID")
        @Expose
        var productID: String? = null,

        @SerializedName("ID")
        @Expose
        var activityId: String? = null,

        @SerializedName("token")
        @Expose
        var token: String? = null,

        @SerializedName("mediaRatioH")
        @Expose
        var mediaRatioH: Int? = null,

        @SerializedName("mediaRatioW")
        @Expose
        var mediaRatioW: Int? = null,

        @SerializedName("media")
        @Expose
        var media: List<SubmitPostMedium>? = null
)
