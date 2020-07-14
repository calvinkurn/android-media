package com.tokopedia.common_tradein.usecase

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradePDP
import com.tokopedia.common_tradein.repository.CommonTradeInRepository
import java.util.HashMap
import javax.inject.Inject

class CheckTradeInUseCase @Inject constructor(val repository: CommonTradeInRepository) {

    fun createRequestParams(tradeInParams: TradeInParams): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables["params"] = tradeInParams
        return variables
    }

    suspend fun checkTradeIn(query: String, tradeInParams: TradeInParams): ValidateTradePDP {
        val variable = createRequestParams(tradeInParams)
        return repository.getGQLData(query, ValidateTradePDP::class.java, variable) as ValidateTradePDP
    }
}