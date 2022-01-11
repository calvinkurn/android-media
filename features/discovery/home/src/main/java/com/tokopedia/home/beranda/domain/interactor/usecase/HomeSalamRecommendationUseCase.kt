package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeDeclineSalamWIdgetRepository
import com.tokopedia.home.util.HomeServerLogger
import javax.inject.Inject

class HomeSalamRecommendationUseCase @Inject constructor(
        private val homeDeclineSalamWIdgetRepository: HomeDeclineSalamWIdgetRepository) {
    suspend fun onDeclineSalamRecommendation(requestParams: Map<String, Int>) {
        try {
            homeDeclineSalamWIdgetRepository.setParams(requestParams)
            homeDeclineSalamWIdgetRepository.executeOnBackground()
        } catch (e: Exception) {
            HomeServerLogger.warning_error_decline_salam_recommendation(e)
        }
    }
}