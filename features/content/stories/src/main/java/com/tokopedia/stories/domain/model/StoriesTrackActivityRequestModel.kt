package com.tokopedia.stories.domain.model

import com.google.gson.annotations.SerializedName

data class StoriesTrackActivityRequestModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("action")
    val action: String
)

enum class StoriesTrackActivityActionType(val value: String) {
    LAST_SEEN("LastSeenStory")
}
