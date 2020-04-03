package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.DeviceDataResponse
import com.tokopedia.tradein.model.DeviceDiagGQL
import com.tokopedia.tradein.model.DeviceDiagParams
import com.tokopedia.tradein.model.KYCDetailGQL
import com.tokopedia.tradein.repository.TradeInRepository
import java.lang.reflect.Type
import javax.inject.Inject

class DiagnosticDataUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
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
        queries.add((GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_get_device_diag)))
        if (tradeInParams?.isUseKyc == 1) {
            queries.add(GraphqlHelper.loadRawString(context.resources,
                    R.raw.gql_get_kyc_status))
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