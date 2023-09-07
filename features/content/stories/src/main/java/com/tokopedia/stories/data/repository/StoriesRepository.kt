package com.tokopedia.stories.data.repository

import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesUiModel

interface StoriesRepository {

    suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesUiModel

    suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetailUiModel

}
