package com.tokopedia.stories.view.model

import com.tokopedia.stories.view.model.StoryUiModel.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryUiModel.StoryGroupUiModel

data class StoryUiState(
    val storyGroup: List<StoryGroupUiModel>,
    val storyDetail: StoryDetailUiModel,
)
