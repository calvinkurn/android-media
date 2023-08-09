package com.tokopedia.feedplus.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.repository.FeedRepository
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 08/08/23.
 */
class FeedRepositoryImpl @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val dispatchers: CoroutineDispatchers
) : FeedRepository {

    override suspend fun getTabs(): FeedTabsModel = withContext(dispatchers.io) {
        val request = feedXHeaderUseCase.apply {
            setRequestParams(
                FeedXHeaderUseCase.createParam(
                    listOf(
                        FeedXHeaderRequestFields.TAB.value
                    )
                )
            )
        }.executeOnBackground()
        return@withContext MapperFeedTabs.transform(request.feedXHeaderData)
    }

    override suspend fun getMeta(): MetaModel = withContext(dispatchers.io) {
        val request = feedXHeaderUseCase.apply {
            setRequestParams(
                FeedXHeaderUseCase.createParam(
                    listOf(
                        FeedXHeaderRequestFields.LIVE.value,
                        FeedXHeaderRequestFields.CREATION.value,
                        FeedXHeaderRequestFields.USER.value
                    )
                )
            )
        }.executeOnBackground()
        return@withContext MapperFeedTabs.transform(request.feedXHeaderData).meta
    }
}
