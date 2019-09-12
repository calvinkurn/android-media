package com.tokopedia.affiliatecommon.data.pojo.submitpost.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-08-13.
 */
data class Meta(
        @SerializedName("followers")
        @Expose
        var followers: Int = 0,

        @SerializedName("content")
        @Expose
        var content: Content = Content()
)