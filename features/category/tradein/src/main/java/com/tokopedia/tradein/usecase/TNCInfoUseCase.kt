package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.model.TnCInfoModel
import com.tokopedia.tradein.raw.GQL_FETCH_TNC
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

@GqlQuery("GqlFetchTnc", GQL_FETCH_TNC)
class TNCInfoUseCase @Inject constructor(
        private val repository: TradeInRepository) {

    suspend fun getTNCInfo(type: Int): TnCInfoModel {
        return repository.getGQLData(GqlFetchTnc.GQL_QUERY, TnCInfoModel::class.java, createRequestParams(type))
    }

    fun createRequestParams(type: Int): Map<String, Any> {
        return mapOf("type" to type)
    }
}