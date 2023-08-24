package com.tokopedia.stories.data.repository

import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.view.model.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryGroupUiModel

interface StoryRepository {

    suspend fun getInitialStoryData(data: StoryRequestModel): StoryGroupUiModel

    suspend fun getStoryDetailData(data: StoryRequestModel): StoryDetailUiModel

}
