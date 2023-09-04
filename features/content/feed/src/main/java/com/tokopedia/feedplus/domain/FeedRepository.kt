package com.tokopedia.feedplus.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.mapper.MapperFeedXHome
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.model.PostSourceModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 08/08/23.
 */
class FeedRepository @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val mapperFeedTabs: MapperFeedTabs,
    private val mapperFeed: MapperFeedXHome,
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

    suspend fun getPost(source: String, cursor: String = ""): FeedModel {
        val response = feedXHomeUseCase(
            feedXHomeUseCase.createParams(
                source,
                cursor
            )
        )
        return mapperFeed.transform(response)
    }

    suspend fun getRelevantPosts(postSourceModel: PostSourceModel): FeedModel {
        val response = feedXHomeUseCase(
            feedXHomeUseCase.createParamsWithId(postSourceModel.id, postSourceModel.source)
        )
        return mapperFeed.transform(response)
    }
}
