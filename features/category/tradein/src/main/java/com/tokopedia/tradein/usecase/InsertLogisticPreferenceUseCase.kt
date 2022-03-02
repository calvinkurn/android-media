package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.model.InsertTradeInLogisticModel
import com.tokopedia.tradein.raw.GQL_INSERT_LOGISTIC_PREFERENCE
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

@GqlQuery("GqlInsertLogisticPreference", GQL_INSERT_LOGISTIC_PREFERENCE)
class InsertLogisticPreferenceUseCase @Inject constructor(
    private val repository: TradeInRepository
) {

    suspend fun insertLogistic(): InsertTradeInLogisticModel {
        return repository.getGQLData(GqlInsertLogisticPreference.GQL_QUERY, InsertTradeInLogisticModel::class.java, createRequestParams())
    }

    private fun createRequestParams(): Map<String, Any> {
        return mapOf()
    }
}