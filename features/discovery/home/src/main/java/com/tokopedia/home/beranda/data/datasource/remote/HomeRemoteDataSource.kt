package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import kotlinx.coroutines.withContext

class HomeRemoteDataSource(
        private val dispatchers: CoroutineDispatchers,
        private val getHomeDynamicChannelsRepository: GetHomeDynamicChannelsRepository,
        private val getHomeDataUseCase: GetHomeDataUseCase,
        private val getAtfDataUseCase: GetHomeAtfUseCase,
        private val getHomeFlagUseCase: GetHomeFlagUseCase,
        private val getHomePageBannerUseCase: GetHomePageBannerUseCase,
        private val getHomeIconRepository: GetHomeIconRepository,
        private val getHomeTickerRepository: GetHomeTickerRepository
) {
    suspend fun getHomeData(): HomeData? = withContext(dispatchers.io) {
        getHomeDataUseCase.executeOnBackground()
    }

    suspend fun getHomeFlagUseCase(): HomeFlagData? = withContext(dispatchers.io) {
        getHomeFlagUseCase.executeOnBackground()
    }

    suspend fun getHomePageBannerUseCase(): HomeBannerData? = withContext(dispatchers.io) {
        getHomePageBannerUseCase.executeOnBackground()
    }

    suspend fun getHomeIconUseCase(param: String, locationParams: String): HomeIconData = withContext(dispatchers.io) {
        getHomeIconRepository.getIconData(param, locationParams)
    }

    suspend fun getHomeTickerUseCase(locationParams: String): HomeTickerData? = withContext(dispatchers.io) {
        getHomeTickerRepository.getTickerData(locationParams)
    }

    suspend fun getAtfDataUseCase(): HomeAtfData? = withContext(dispatchers.io) {
        getAtfDataUseCase.executeOnBackground()
    }
    suspend fun getDynamicChannelData(groupIds: String = "",
                                      token: String = "",
                                      numOfChannel: Int = 0,
                                      params: String = "",
                                      locationParams: String = "",
                                      doQueryHash: Boolean = false
    ): HomeChannelData = getHomeDynamicChannelsRepository.getDynamicChannelData(
            GetHomeDynamicChannelsRepository.buildParams(
                    groupIds = groupIds,
                    token = token,
                    numOfChannel = numOfChannel,
                    queryParams = params,
                    locationParams = locationParams,
                    doQueryHash = doQueryHash
            )
    )
}