package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeDeclineRechargeRecommendationRepository
import com.tokopedia.home.util.HomeServerLogger
import javax.inject.Inject

class HomeRechargeRecommendationUseCase @Inject constructor(
        private val homeDeclineRechargeRecommendationRepository: HomeDeclineRechargeRecommendationRepository) {
    suspend fun onDeclineRechargeRecommendation(requestParams: Map<String, String>) {
        try {
            homeDeclineRechargeRecommendationRepository.setParams(requestParams)
            homeDeclineRechargeRecommendationRepository.executeOnBackground()
        } catch (e: Exception) {
            HomeServerLogger.warning_error_decline_recharge_recommendation(e)
        }
    }
}