package com.tokopedia.tradein.usecase

import com.tokopedia.tradein.model.RatesV3DataModel
import com.tokopedia.tradein.raw.GQL_RATES_V3
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

class RatesV3UseCase @Inject constructor(
        private val repository: TradeInRepository) {

    suspend fun getRatesV3(weight: String, destination: String, origin: String): RatesV3DataModel {
        return repository.getGQLData(GQL_RATES_V3, RatesV3DataModel::class.java, createRequestParams(weight, destination, origin))
    }

    fun createRequestParams(weight: String, destination: String, origin: String): Map<String, Any> {
        return mapOf(
                "spids" to "48",
                "tradeIn" to 2,
                "weight" to weight,
                "destination" to destination,
                "origin" to origin
        )
    }
}