package com.tokopedia.stories.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.stories.data.mapper.StoriesMapperImpl
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.domain.usecase.StoriesDetailsUseCase
import com.tokopedia.stories.domain.usecase.StoriesGroupsUseCase
import com.tokopedia.stories.domain.usecase.StoriesTrackActivityUseCase
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val storiesGroupsUseCase: StoriesGroupsUseCase,
    private val storiesDetailsUSeCase: StoriesDetailsUseCase,
    private val storiesTrackActivityUseCase: StoriesTrackActivityUseCase,
    private val mapper: StoriesMapperImpl,
) : StoriesRepository {

    override suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesUiModel = withContext(dispatchers.io) {
        val storiesGroupData = storiesGroupsUseCase(data)
        val storiesDetailData = storiesDetailsUSeCase(data)
        return@withContext mapper.mapStoriesInitialData(storiesGroupData, storiesDetailData)
    }

    override suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetailUiModel = withContext(dispatchers.io) {
        val storiesDetailData = storiesDetailsUSeCase(data)
        return@withContext mapper.mapStoriesDetailRequest(storiesDetailData)
    }

    override suspend fun setStoriesTrackActivity(data: StoriesTrackActivityRequestModel): Boolean = withContext(dispatchers.io) {
        val storiesTrackActivity = storiesTrackActivityUseCase(data)
        return@withContext storiesTrackActivity.data.isSuccess
    }

}
