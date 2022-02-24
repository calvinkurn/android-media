package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.model.PromoTradeInModel
import com.tokopedia.tradein.raw.GQL_PROMO
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

@GqlQuery("GqlPromo", GQL_PROMO)
class PromoUseCase @Inject constructor(
    private val repository: TradeInRepository
) {

    suspend fun getPromo(code: String): PromoTradeInModel {
        return repository.getGQLData(GqlPromo.GQL_QUERY, PromoTradeInModel::class.java, createRequestParams(code))
    }

    private fun createRequestParams(code: String): Map<String, Any> {
        return mapOf(
            TradeinConstants.UseCase.KEY_CODE to code
        )
    }
}