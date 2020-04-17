package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class DiscussionDataResponseWrapper(
        @SerializedName("discussionDataByProductID")
        val discussionData: DiscussionDataResponse = DiscussionDataResponse()
)