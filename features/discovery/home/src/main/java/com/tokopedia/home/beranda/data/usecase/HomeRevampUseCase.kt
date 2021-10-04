package com.tokopedia.home.beranda.data.usecase

import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.repository.HomeRevampRepository
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRevampUseCase @Inject constructor(
        private val homeRepository: HomeRevampRepository,
        private val homeDataMapper: HomeDataMapper
) {
    fun getHomeData(): Flow<HomeDataModel?> {
        var firstTimeDataHasBeenConsumed = false
        var haveCacheData: Boolean = false

        return homeRepository.getHomeData().map { data ->
            if (!firstTimeDataHasBeenConsumed) {
                //fetch new data
                firstTimeDataHasBeenConsumed = true
                homeDataMapper.mapToHomeRevampViewModel(homeData = data, isCache = true, haveCachedData = haveCacheData)
            }
            //not first time, emit real data from network
            else {
                homeDataMapper.mapToHomeRevampViewModel(homeData = data, isCache = false, haveCachedData = haveCacheData)
            }
        }
    }

    fun getHomeCachedData(): HomeDataModel? {
        val defaultFlag = HomeFlag()
        defaultFlag.addFlag(HomeFlag.HAS_TOKOPOINTS_STRING, true)
        return homeDataMapper.mapToHomeRevampViewModel(HomeData(
                atfData = homeRepository.getHomeCachedAtfData(),
                isProcessingDynamicChannel = true, homeFlag = defaultFlag),
                isCache = true,
                addShimmeringChannel = true,
                isLoadingAtf = true
        )
    }

    suspend fun onDynamicChannelExpired(groupId: String) = homeRepository.onDynamicChannelExpired(groupId = groupId)

    fun updateHomeData() = homeRepository.updateHomeData()

    fun deleteHomeData() = homeRepository.deleteHomeData()
}