package com.tokopedia.affiliate.feature.dashboard.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-06.
 */
data class DashboardSubtitlePojo(
        @SerializedName("key")
        @Expose
        val key: String = "",

        @SerializedName("text")
        @Expose
        val text: String = ""
)