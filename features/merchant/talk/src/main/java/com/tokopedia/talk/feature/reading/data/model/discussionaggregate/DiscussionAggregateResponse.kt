package com.tokopedia.talk.feature.reading.data.model.discussionaggregate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionAggregateResponse(
        @SerializedName("discussionAggregateByProductID")
        @Expose
        val discussionAggregateResponse: DiscussionAggregate = DiscussionAggregate()
)