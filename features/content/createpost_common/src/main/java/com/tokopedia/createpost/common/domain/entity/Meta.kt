package com.tokopedia.createpost.common.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-08-13.
 */
data class Meta(
        @SerializedName("content")
        @Expose
        var content: Content = Content()
)
