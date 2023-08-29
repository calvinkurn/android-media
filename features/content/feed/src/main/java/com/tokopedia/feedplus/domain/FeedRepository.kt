package com.tokopedia.feedplus.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 08/08/23.
 */
class FeedRepository @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val mapperFeedTabs: MapperFeedTabs,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun getTabs(activeTabSource: ActiveTabSource): FeedTabsModel = withContext(dispatchers.io) {
        val request = feedXHeaderUseCase.apply {
            setRequestParams(
                FeedXHeaderUseCase.createParam(
                    listOf(
                        FeedXHeaderRequestFields.TAB.value
                    )
                )
            )
        }.executeOnBackground()
        return@withContext mapperFeedTabs.transform(request.feedXHeaderData, activeTabSource)
    }

    suspend fun getMeta(activeTabSource: ActiveTabSource): MetaModel = withContext(dispatchers.io) {
        val request = feedXHeaderUseCase.apply {
            setRequestParams(
                FeedXHeaderUseCase.createParam(
                    listOf(
                        FeedXHeaderRequestFields.LIVE.value,
                        FeedXHeaderRequestFields.CREATION.value,
                        FeedXHeaderRequestFields.USER.value,
                        FeedXHeaderRequestFields.BROWSE.value
                    )
                )
            )
        }.executeOnBackground()
        return@withContext mapperFeedTabs.transform(request.feedXHeaderData, activeTabSource).meta
    }
}
