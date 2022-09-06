package com.tokopedia.home.beranda.data.datasource.remote

import android.os.Bundle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.interactor.repository.*
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import kotlinx.coroutines.withContext

class HomeRemoteDataSource(
        private val dispatchers: CoroutineDispatchers,
        private val homeDynamicChannelsRepository: HomeDynamicChannelsRepository,
        private val homeDataRepository: HomeDataRepository,
        private val atfDataRepository: HomeAtfRepository,
        private val homeFlagRepository: HomeFlagRepository,
        private val homePageBannerRepository: HomePageBannerRepository,
        private val homeIconRepository: HomeIconRepository,
        private val homeTickerRepository: HomeTickerRepository
) {
    suspend fun getHomeData(): HomeData? = withContext(dispatchers.io) {
        homeDataRepository.executeOnBackground()
    }

    suspend fun getHomeFlagUseCase(): HomeFlagData? = withContext(dispatchers.io) {
        homeFlagRepository.executeOnBackground()
    }

    suspend fun getHomeIconUseCase(param: String, locationParams: String): HomeIconData = withContext(dispatchers.io) {
        homeIconRepository.getIconData(param, locationParams)
    }

    suspend fun getHomeTickerUseCase(locationParams: String): HomeTickerData? = withContext(dispatchers.io) {
        homeTickerRepository.getTickerData(locationParams)
    }

    suspend fun getAtfDataUseCase(): HomeAtfData? = withContext(dispatchers.io) {
        atfDataRepository.executeOnBackground()
    }
    suspend fun getDynamicChannelData(groupIds: String = "",
                                      token: String = "",
                                      numOfChannel: Int = 0,
                                      params: String = "",
                                      locationParams: String = "",
                                      channelIds: String = "",
                                      doQueryHash: Boolean = false
    ): HomeChannelData {
        val bundle = Bundle().apply {
            putString(HomeDynamicChannelsRepository.GROUP_IDS, groupIds)
            putString(HomeDynamicChannelsRepository.TOKEN, token)
            putInt(HomeDynamicChannelsRepository.NUM_OF_CHANNEL, numOfChannel)
            putString(HomeDynamicChannelsRepository.PARAMS, params)
            putString(HomeDynamicChannelsRepository.LOCATION, locationParams)
            putString(HomeDynamicChannelsRepository.CHANNEL_IDS, channelIds)
        }
        return homeDynamicChannelsRepository.getRemoteData(bundle)
    } 
    suspend fun getHomePageBannerData(): HomeBannerData? = withContext(dispatchers.io) {
        homePageBannerRepository.executeOnBackground()
    }
}