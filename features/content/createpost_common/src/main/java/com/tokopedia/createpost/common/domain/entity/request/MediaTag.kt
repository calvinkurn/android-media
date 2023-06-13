package com.tokopedia.createpost.common.domain.entity.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by milhamj on 08/03/19.
 */
data class MediaTag(
        @SerializedName("tagType")
        @Expose
        val type: String = "",

        @SerializedName("tagContent")
        @Expose
        val content: String = "",

        @SerializedName("position")
        @Expose
        var position: List<Double> = arrayListOf()

        )
