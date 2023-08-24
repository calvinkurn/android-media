package com.tokopedia.stories.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.stories.data.mapper.StoryMapperImpl
import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.domain.usecase.StoryDetailsUseCase
import com.tokopedia.stories.domain.usecase.StoryGroupsUseCase
import com.tokopedia.stories.view.model.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryGroupUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val storyGroupsUseCase: StoryGroupsUseCase,
    private val storyDetailsUSeCase: StoryDetailsUseCase,
    private val mapper: StoryMapperImpl,
) : StoryRepository {

    override suspend fun getInitialStoryData(data: StoryRequestModel): StoryGroupUiModel = withContext(dispatchers.io) {
        val storyGroupData = storyGroupsUseCase(data)
        val storyDetailData = storyDetailsUSeCase(data)
        return@withContext mapper.mapInitialStoryData(storyGroupData, storyDetailData)
    }

    override suspend fun getStoryDetailData(data: StoryRequestModel): StoryDetailUiModel = withContext(dispatchers.io) {
        val storyDetailData = storyDetailsUSeCase(data)
        return@withContext mapper.mapDetailStoryRequest(storyDetailData)
    }

}
