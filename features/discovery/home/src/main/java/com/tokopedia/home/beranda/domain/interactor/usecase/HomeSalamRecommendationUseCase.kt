package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeDeclineSalamWIdgetRepository
import javax.inject.Inject

class HomeSalamRecommendationUseCase @Inject constructor(
        private val homeDeclineSalamWIdgetRepository: HomeDeclineSalamWIdgetRepository) {
    suspend fun onDeclineSalamRecommendation(requestParams: Map<String, Int>) {
        homeDeclineSalamWIdgetRepository.setParams(requestParams)
        homeDeclineSalamWIdgetRepository.executeOnBackground()
    }
}