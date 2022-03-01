package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
import com.tokopedia.digital_product_detail.domain.repository.RechargeRecommendationRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeRecommendationUseCase
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeRecommendationRepositoryImpl @Inject constructor(
    private val getRechargeRecommendationUseCase: GetRechargeRecommendationUseCase,
    private val mapper: DigitalDenomMapper,
    private val dispatchers: CoroutineDispatchers
): RechargeRecommendationRepository {

    override suspend fun getRecommendations(
        clientNumbers: List<String>,
        dgCategoryIds: List<Int>,
        isBigRecommendation: Boolean
    ): List<RecommendationCardWidgetModel> = withContext(dispatchers.io) {

        val data = getRechargeRecommendationUseCase.apply {
            createParams(clientNumbers, dgCategoryIds)
        }.executeOnBackground()

        return@withContext data.recommendationData.items.map {
            mapper.mapDigiPersoToRecommendation(it, isBigRecommendation)
        }
    }
}