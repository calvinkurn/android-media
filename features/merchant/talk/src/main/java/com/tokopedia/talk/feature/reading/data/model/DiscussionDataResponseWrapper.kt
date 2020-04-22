package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionDataResponseWrapper(
        @SerializedName("discussionDataByProductID")
        @Expose
        val discussionData: DiscussionDataResponse = DiscussionDataResponse()
)