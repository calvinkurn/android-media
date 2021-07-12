package com.tokopedia.tradein.usecase

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.tradein.model.DeviceDataResponse
import com.tokopedia.tradein.model.DeviceDiagGQL
import com.tokopedia.tradein.model.DeviceDiagParams
import com.tokopedia.tradein.raw.GQL_GET_DEVICE_DIAG
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

@GqlQuery("GqlGetDeviceDiag", GQL_GET_DEVICE_DIAG)
class DiagnosticDataUseCase @Inject constructor(
        private val repository: TradeInRepository) {

    fun createRequestParamsDeviceDiag(tradeInParams: TradeInParams?, tradeInType: Int): HashMap<String, Any> {
        val productid = tradeInParams?.productId ?: 0
        val deviceid = tradeInParams?.deviceId
        val newprice = tradeInParams?.newPrice ?: 0
        val params = DeviceDiagParams()
        params.productId = productid
        params.deviceId = deviceid
        params.newPrice = newprice
        params.tradeInType = tradeInType
        val variables = HashMap<String, Any>()
        variables["params"] = params
        return variables
    }

    suspend fun getDiagnosticData(tradeInParams: TradeInParams?, tradeInType: Int): DeviceDataResponse {
        return repository.getGQLData(GqlGetDeviceDiag.GQL_QUERY, DeviceDiagGQL::class.java, createRequestParamsDeviceDiag(tradeInParams, tradeInType), CacheType.ALWAYS_CLOUD).diagResponse
    }
}