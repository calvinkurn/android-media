package com.tokopedia.stories.domain.model

data class StoriesTrackActivityRequestModel(
    val id: String,
    val action: String,
)

enum class StoriesTrackActivityActionType(val value: String) {
    LAST_SEEN("LastSeenStory")
}
