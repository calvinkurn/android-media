package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.home.beranda.common.HomeDispatcherProvider
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.domain.interactor.GetDynamicChannelsUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeAtfUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import kotlinx.coroutines.withContext

class HomeRemoteDataSource(
        private val dispatchers: HomeDispatcherProvider,
        private val getDynamicChannelsUseCase: GetDynamicChannelsUseCase,
        private val getHomeDataUseCase: GetHomeDataUseCase,
        private val getAtfDataUseCase: GetHomeAtfUseCase
) {
    suspend fun getHomeData(): HomeData? = withContext(dispatchers.io()) {
        getHomeDataUseCase.executeOnBackground()
    }

    suspend fun getAtfDataUseCase(): HomeAtfData? = withContext(dispatchers.io()) {
        getAtfDataUseCase.executeOnBackground()
    }

    suspend fun getDynamicChannelData(groupIds: String = "", token: String = "", numOfChannel: Int = 0): HomeChannelData = withContext(dispatchers.io()) {
        getDynamicChannelsUseCase.setParams(
                groupIds, token, numOfChannel
        )
        getDynamicChannelsUseCase.executeOnBackground()
    }
}