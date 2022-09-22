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
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
class FeedPlusRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val useCase: GraphqlUseCase<FeedTabs.Response>,
    private val getWhitelistUseCase: GetWhitelistNewUseCase,
    private val getContentFormForFeedUseCase: GetContentFormForFeedUseCase
) : FeedPlusRepository {

    override suspend fun getWhitelist(): WhitelistQuery = withContext(dispatchers.io) {
        getWhitelistUseCase.execute(WHITELIST_ENTRY_POINT)
    }
}
