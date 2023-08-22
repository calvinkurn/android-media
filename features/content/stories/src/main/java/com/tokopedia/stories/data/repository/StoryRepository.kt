package com.tokopedia.stories.data.repository

import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.view.model.StoryUiModel

interface StoryRepository {

    suspend fun getInitialStoryData(data: StoryRequestModel): StoryUiModel

}
