package com.tokopedia.stories.domain.model.track

import com.google.gson.annotations.SerializedName

data class StoriesTrackActivityResponseModel(
    @SerializedName("contentTrackActivity")
    val data: ContentTrackActivity = ContentTrackActivity(),
) {

    data class ContentTrackActivity(
        @SerializedName("success")
        val isSuccess: Boolean = false,
    )
}

