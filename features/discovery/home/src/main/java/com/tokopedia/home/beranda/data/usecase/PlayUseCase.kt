package com.tokopedia.home.beranda.data.usecase

import com.tokopedia.v2.home.base.HomeRepository
import com.tokopedia.v2.home.model.vo.PlayCardDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayUseCase @Inject constructor(
        private val homeRepository: HomeRepository
){
    suspend fun execute(): Flow<PlayCardDataModel> = homeRepository.getPlayCard().map {
        PlayCardDataModel(it)
    }
}