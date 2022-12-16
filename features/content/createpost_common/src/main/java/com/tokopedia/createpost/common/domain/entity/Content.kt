package com.tokopedia.createpost.common.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-08-13.
 */
data class Content(
        @SerializedName("activityID")
        @Expose
        var activityID: String = "",

        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("description")
        @Expose
        var description: String = "",

        @SerializedName("url")
        @Expose
        var url: String = ""
)
