package com.tokopedia.affiliatecommon.data.pojo.trackaffiliate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by milhamj on 10/17/18.
 */
data class TrackAffiliatePojo(
        @SerializedName("message")
        @Expose
        val message: String = ""
)