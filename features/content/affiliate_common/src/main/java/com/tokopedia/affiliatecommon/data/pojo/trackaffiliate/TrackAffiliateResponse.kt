package com.tokopedia.affiliatecommon.data.pojo.trackaffiliate


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrackAffiliateResponse(
        @SerializedName("topadsAffiliateTracker")
        @Expose
        val topadsAffiliateTracker: TopadsAffiliateTracker? = TopadsAffiliateTracker()
)