package com.tokopedia.tradein.usecase

import com.tokopedia.tradein.TradeinConstants.UseCase.KEY_DESTINATION
import com.tokopedia.tradein.TradeinConstants.UseCase.KEY_ORIGIN
import com.tokopedia.tradein.TradeinConstants.UseCase.KEY_SPID
import com.tokopedia.tradein.TradeinConstants.UseCase.KEY_TRADEIN
import com.tokopedia.tradein.TradeinConstants.UseCase.KEY_WEIGHT
import com.tokopedia.tradein.TradeinConstants.UseCase.SP_ID
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
                KEY_SPID to SP_ID,
                KEY_TRADEIN to 2,
                KEY_WEIGHT to weight,
                KEY_DESTINATION to destination,
                KEY_ORIGIN to origin
        )
    }
}