package com.tokopedia.home.beranda.data.usecase

import com.tokopedia.home.beranda.data.repository.HomeRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(
        private val homeRepository: HomeRepository
) {
    fun getHomeData() = homeRepository.getHomeData()

    suspend fun updateHomeData() = homeRepository.updateHomeData()

    fun sendGeolocationInfo() = homeRepository.sendGeolocationInfo()
}