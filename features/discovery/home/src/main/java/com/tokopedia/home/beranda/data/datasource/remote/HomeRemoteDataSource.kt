package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.query.HomeQuery
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import kotlinx.coroutines.withContext

class HomeRemoteDataSource(
        private val graphqlRepository: GraphqlRepository,
        private val dispatchers: HomeDispatcherProvider,
        private val getDynamicChannelsUseCase: GetDynamicChannelsUseCase,
        private val getHomeDataUseCase: GetHomeDataUseCase
) {
    suspend fun getHomeData(): HomeData = withContext(dispatchers.io()) {
        getHomeDataUseCase.executeOnBackground()
    }

    suspend fun getDynamicChannelData(groupIds: String = "", token: String = "", numOfChannel: Int = 0): HomeChannelData = withContext(dispatchers.io()) {
        getDynamicChannelsUseCase.setParams(
                groupIds, token, numOfChannel
        )
        getDynamicChannelsUseCase.executeOnBackground()
    }
}