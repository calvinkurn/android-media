package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class DiscussionAggregateResponse(
        @SerializedName("discussionAggregateByProductID")
        val discussionAggregateResponse: DiscussionAggregate = DiscussionAggregate()
)