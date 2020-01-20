package com.tokopedia.kol.feature.comment.data.pojo.get

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kol.feature.comment.data.type.SourceType

/**
 * Created by jegul on 2019-10-28
 */
data class Source(
        @SerializedName("origin")
        @Expose
        val origin: Int = 0,

        @SerializedName("type")
        @Expose
        val type: Int = SourceType.USER.typeInt
)