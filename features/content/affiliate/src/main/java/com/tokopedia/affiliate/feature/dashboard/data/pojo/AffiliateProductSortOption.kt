package com.tokopedia.affiliate.feature.dashboard.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-09.
 */
data class AffiliateProductSortOption(
        @SerializedName("sortVal")
        @Expose
        val sortVal: Int = 0,

        @SerializedName("text")
        @Expose
        val text: String = ""
)