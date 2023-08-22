package com.tokopedia.stories.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.stories.data.mapper.StoryMapper
import com.tokopedia.stories.data.mapper.StoryMapperImpl
import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.domain.usecase.StoryDetailsUseCase
import com.tokopedia.stories.domain.usecase.StoryGroupsUseCase
import com.tokopedia.stories.view.model.StoryUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val storyGroupsUseCase: StoryGroupsUseCase,
    private val storyDetailsUSeCase: StoryDetailsUseCase,
    private val mapper: StoryMapperImpl,
) : StoryRepository {

    override suspend fun getInitialStoryData(data: StoryRequestModel): StoryUiModel = withContext(dispatchers.io) {
        val storyGroupData = storyGroupsUseCase(data)
        val storyDetailData = storyDetailsUSeCase(data)
        return@withContext mapper.mapStoryData(storyGroupData, storyDetailData)
    }

}
