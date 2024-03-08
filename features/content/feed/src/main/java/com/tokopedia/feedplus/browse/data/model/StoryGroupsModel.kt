package com.tokopedia.feedplus.browse.data.model

internal data class StoryGroupsModel(
    val storyList: List<StoryNodeModel>,
    val nextCursor: String,
)
