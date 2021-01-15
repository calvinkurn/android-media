package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import kotlinx.coroutines.withContext

class HomeRemoteDataSource(
        private val dispatchers: HomeDispatcherProvider,
        private val getDynamicChannelsUseCase: GetDynamicChannelsUseCase,
        private val getHomeDataUseCase: GetHomeDataUseCase,
        private val getAtfDataUseCase: GetHomeAtfUseCase,
        private val getHomeFlagUseCase: GetHomeFlagUseCase,
        private val getHomePageBannerUseCase: GetHomePageBannerUseCase,
        private val getHomeIconUseCase: GetHomeIconUseCase,
        private val getHomeTickerUseCase: GetHomeTickerUseCase
) {
    suspend fun getHomeData(): HomeData? = withContext(dispatchers.io()) {
        getHomeDataUseCase.executeOnBackground()
    }

    suspend fun getHomeFlagUseCase(): HomeFlagData? = withContext(dispatchers.io()) {
        getHomeFlagUseCase.executeOnBackground()
    }

    suspend fun getHomePageBannerUseCase(): HomeBannerData? = withContext(dispatchers.io()) {
        getHomePageBannerUseCase.executeOnBackground()
    }

    suspend fun getHomeIconUseCase(param: String): HomeIconData = withContext(dispatchers.io()) {
        getHomeIconUseCase.executeOnBackground(param)
    }

    suspend fun getHomeTickerUseCase(): HomeTickerData? = withContext(dispatchers.io()) {
        getHomeTickerUseCase.executeOnBackground()
    }

    suspend fun getAtfDataUseCase(): HomeAtfData? = withContext(dispatchers.io()) {
        getAtfDataUseCase.executeOnBackground()
    }
    suspend fun getDynamicChannelData(groupIds: String = "",
                                      token: String = "",
                                      numOfChannel: Int = 0,
                                      params: String = "",
                                      doQueryHash: Boolean = false
    ): HomeChannelData = withContext(dispatchers.io()) {
        getDynamicChannelsUseCase.setParams(
                groupIds, token, numOfChannel, params, doQueryHash
        )
        getDynamicChannelsUseCase.executeOnBackground()
    }
}