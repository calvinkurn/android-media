package com.tokopedia.kol.feature.postdetail.data


data class FeedXPostRecommendationResponse(
    var postList: List<Any> = emptyList(),
    var nextCursor: String = "",
)