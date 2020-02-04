package com.tokopedia.home.beranda.data.usecase

import com.tokopedia.home.beranda.data.repository.HomeRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayLiveDynamicUseCase @Inject constructor(
        private val homeRepository: HomeRepository
){
    fun execute() = homeRepository.getPlayChannel().map {
        val channels = it.playDynamicData.playData.playChannels
        if(channels.isEmpty()) error("Empty Data")
        channels
    }
}