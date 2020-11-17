package com.tokopedia.tradein.usecase

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.model.DeviceDataResponse
import com.tokopedia.tradein.model.DeviceDiagGQL
import com.tokopedia.tradein.model.DeviceDiagParams
import com.tokopedia.tradein.model.KYCDetailGQL
import com.tokopedia.tradein.raw.GQL_GET_DEVICE_DIAG
import com.tokopedia.tradein.raw.GQL_KYC_STATUS
import com.tokopedia.tradein.repository.TradeInRepository
import java.lang.reflect.Type
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

    fun createRequestParamsKYCStatus(): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables["projectID"] = 4
        return variables
    }

    fun getQueries(tradeInParams: TradeInParams?): MutableList<String> {
        val queries: MutableList<String> = ArrayList()
        queries.add(GqlGetDeviceDiag.GQL_QUERY)
        if (tradeInParams?.isUseKyc == 1) {
            queries.add(GQL_KYC_STATUS)
        }
        return queries
    }

    suspend fun getDiagnosticData(tradeInParams: TradeInParams?, tradeInType: Int): DeviceDataResponse? {
        val request: MutableList<HashMap<String, Any>> = ArrayList()
        val type: MutableList<Type> = ArrayList()

        request.add(createRequestParamsDeviceDiag(tradeInParams, tradeInType))
        type.add(DeviceDiagGQL::class.java)
        if (tradeInParams?.isUseKyc == 1) {
            request.add(createRequestParamsKYCStatus())
            type.add(KYCDetailGQL::class.java)
        }
        val response = repository.getGQLData(getQueries(tradeInParams), type, request)
        response?.let {
            val deviceDiagGQL = it.getData<DeviceDiagGQL>(DeviceDiagGQL::class.java)
            if (deviceDiagGQL != null) {
                val deviceDataResponse = deviceDiagGQL.diagResponse
                val kycDetailGQL = it.getData<KYCDetailGQL>(KYCDetailGQL::class.java)
                if (deviceDataResponse != null) {
                    if (kycDetailGQL != null) {
                        val kycDetails = kycDetailGQL.kycDetails
                        if (kycDetails != null)
                            deviceDataResponse.kycDetails = kycDetails
                    }
                    return deviceDataResponse
                } else
                    throw RuntimeException("")
            } else
                throw RuntimeException("")
        }
        return null
    }
}