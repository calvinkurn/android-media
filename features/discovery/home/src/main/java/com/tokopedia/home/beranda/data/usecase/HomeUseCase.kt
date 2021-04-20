package com.tokopedia.home.beranda.data.usecase

import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(
        private val homeRepository: HomeRepository,
        private val homeDataMapper: HomeDataMapper
) {
    fun getHomeData(): Flow<HomeDataModel?> = flow {
        var firstTimeDataHasBeenConsumed = false

        homeRepository.getHomeData().collect { data->
            if (!firstTimeDataHasBeenConsumed) {
                //first time observe, get latest data from cache
                emit(homeDataMapper.mapToHomeViewModel(data, true))
                //fetch new data
                firstTimeDataHasBeenConsumed = true
            }
            //not first time, emit real data from network
            else emit(homeDataMapper.mapToHomeViewModel(data, false))
        }
    }

    suspend fun onDynamicChannelExpired(groupId: String) = homeRepository.onDynamicChannelExpired(groupId = groupId)

    fun updateHomeData() = homeRepository.updateHomeData()

    fun deleteHomeData() = homeRepository.deleteHomeData()
}