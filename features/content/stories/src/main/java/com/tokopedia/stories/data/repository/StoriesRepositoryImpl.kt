package com.tokopedia.stories.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.stories.data.mapper.StoriesMapperImpl
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.domain.usecase.StoriesDetailsUseCase
import com.tokopedia.stories.domain.usecase.StoriesGroupsUseCase
import com.tokopedia.stories.domain.usecase.StoriesTrackActivityUseCase
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val storiesGroupsUseCase: StoriesGroupsUseCase,
    private val storiesDetailsUseCase: StoriesDetailsUseCase,
    private val storiesTrackActivityUseCase: StoriesTrackActivityUseCase,
    private val mapper: StoriesMapperImpl,
) : StoriesRepository {

    override suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesUiModel = withContext(dispatchers.io) {
        val groupRequest = asyncCatchError(block = { storiesGroupsUseCase(data) }) { throw it }
        val detailRequest = asyncCatchError(block = { storiesDetailsUseCase(data) }) { throw it }
        val groupResult = groupRequest.await() ?: throw Throwable("Data Group is null")
        val detailResult = detailRequest.await() ?: throw Throwable("Data Detail is null")
        return@withContext mapper.mapStoriesInitialData(groupResult, detailResult)
    }

    override suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetail = withContext(dispatchers.io) {
        val detailRequest = storiesDetailsUseCase(data)
        return@withContext mapper.mapStoriesDetailRequest("", detailRequest)
    }

    override suspend fun setStoriesTrackActivity(data: StoriesTrackActivityRequestModel): Boolean = withContext(dispatchers.io) {
        val trackActivityRequest = storiesTrackActivityUseCase(data)
        return@withContext trackActivityRequest.data.isSuccess
    }

}
