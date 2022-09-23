package com.tokopedia.feedplus.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.feedplus.domain.usecase.GetContentFormForFeedUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedcomponent.domain.usecase.WHITELIST_ENTRY_POINT
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlResponse
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
class FeedPlusRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getDynamicTabsUseCase: GraphqlUseCase<FeedTabs.Response>,
    private val getWhitelistUseCase: GetWhitelistNewUseCase,
    private val getContentFormForFeedUseCase: GetContentFormForFeedUseCase
) : FeedPlusRepository {

    override suspend fun getWhitelist(): WhitelistQuery = withContext(dispatchers.io) {
        getWhitelistUseCase.execute(WHITELIST_ENTRY_POINT)
    }

    override suspend fun getDynamicTabs(): FeedTabs = withContext(dispatchers.io) {
        getDynamicTabsUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        getDynamicTabsUseCase.executeOnBackground().feedTabs
    }

    override suspend fun clearDynamicTabCache() {
        getDynamicTabsUseCase.clearCache()
    }

    override fun getFeedContentForm(subscriber: Subscriber<GraphqlResponse>) {
        getContentFormForFeedUseCase.clearRequest()
        getContentFormForFeedUseCase.execute(
            GetContentFormForFeedUseCase.createRequestParams(mutableListOf(),"",""),
            subscriber
        )
    }
}
