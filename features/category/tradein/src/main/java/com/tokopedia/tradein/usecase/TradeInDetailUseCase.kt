package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.raw.GQL_TRADE_IN_DETAIL
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

@GqlQuery("GqlTradeInDetail", GQL_TRADE_IN_DETAIL)
class TradeInDetailUseCase @Inject constructor(
    private val repository: TradeInRepository
) {

    suspend fun getTradeInDetail(laku6DeviceModel: Laku6DeviceModel): TradeInDetailModel {
        return repository.getGQLData(GqlTradeInDetail.GQL_QUERY, TradeInDetailModel::class.java, createRequestParams(laku6DeviceModel))
    }

    fun createRequestParams(laku6DeviceModel: Laku6DeviceModel): Map<String, Any> {
        return mapOf()
    }
}