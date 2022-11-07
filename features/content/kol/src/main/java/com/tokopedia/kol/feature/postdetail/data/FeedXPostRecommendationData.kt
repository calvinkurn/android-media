package com.tokopedia.kol.feature.postdetail.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard

data class FeedXPostRecommendationData(
    @SerializedName("feedXPostRecommendation")
    var feedXPostRecommendation: FeedXPostRecommendation
)
data class FeedXPostRecommendation(
    @SerializedName("posts")
    var posts: List<FeedXCard>,
    @SerializedName("nextCursor")
    var nextCursor: String,
)
