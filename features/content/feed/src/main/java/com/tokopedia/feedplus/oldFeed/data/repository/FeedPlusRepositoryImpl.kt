package com.tokopedia.feedplus.oldFeed.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.usecase.GetWhiteListUseCase
import com.tokopedia.feedplus.oldFeed.data.pojo.FeedTabs
import com.tokopedia.feedplus.oldFeed.domain.repository.FeedPlusRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
class FeedPlusRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getDynamicTabsUseCase: GraphqlUseCase<FeedTabs.Response>,
    private val getWhitelistUseCase: GetWhiteListUseCase
) : FeedPlusRepository {

    override suspend fun getWhitelist(): GetCheckWhitelistResponse = withContext(dispatchers.io) {
        getWhitelistUseCase(GetWhiteListUseCase.WhiteListType.EntryPoint)
    }

    override suspend fun getDynamicTabs(): FeedTabs = withContext(dispatchers.io) {
        getDynamicTabsUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        getDynamicTabsUseCase.executeOnBackground().feedTabs
    }

    override suspend fun clearDynamicTabCache() {
        getDynamicTabsUseCase.clearCache()
    }
}
