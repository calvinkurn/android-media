package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeDeclineRechargeRecommendationRepository
import javax.inject.Inject

class HomeRechargeRecommendationUseCase @Inject constructor(
        private val homeDeclineRechargeRecommendationRepository: HomeDeclineRechargeRecommendationRepository) {
    suspend fun onDeclineRechargeRecommendation(requestParams: Map<String, String>) {
        homeDeclineRechargeRecommendationRepository.setParams(requestParams)
        homeDeclineRechargeRecommendationRepository.executeOnBackground()
    }
}