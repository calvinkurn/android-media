package com.tokopedia.stories.data.repository

import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel

interface StoriesRepository {

    suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesGroupUiModel

    suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetailUiModel

    suspend fun deleteStory(storyId: String) : Boolean
}
