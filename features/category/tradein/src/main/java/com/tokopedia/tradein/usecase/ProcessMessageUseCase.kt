package com.tokopedia.tradein.usecase

import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.model.DeviceAttr
import com.tokopedia.tradein.model.DeviceDiagInput
import com.tokopedia.tradein.model.DeviceDiagInputResponse
import com.tokopedia.tradein.model.DeviceDiagnostics
import com.tokopedia.tradein.raw.GQL_INSERT_DEVICE_DIAG
import com.tokopedia.tradein.repository.TradeInRepository
import com.tokopedia.tradein.view.viewcontrollers.activity.BaseTradeInActivity.TRADEIN_OFFLINE
import java.util.*
import javax.inject.Inject

@GqlQuery("GqlInsertDeviceDiag",GQL_INSERT_DEVICE_DIAG)
class ProcessMessageUseCase @Inject constructor(
        private val repository: TradeInRepository)  {

    private var tradeInType: Int = TRADEIN_OFFLINE

    fun createRequestParams(tradeInParams : TradeInParams, diagnostics: DeviceDiagnostics): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        try {
            val imei = ArrayList<String>()
            imei.add(diagnostics.imei)
            val deviceAttr = DeviceAttr()
            deviceAttr.brand = diagnostics.brand
            deviceAttr.grade = diagnostics.grade
            deviceAttr.imei = imei
            deviceAttr.model = diagnostics.model
            deviceAttr.modelId = diagnostics.modelId
            deviceAttr.ram = diagnostics.ram
            deviceAttr.storage = diagnostics.storage
            val deviceDiagInput = DeviceDiagInput()
            deviceDiagInput.uniqueCode = diagnostics.tradeInUniqueCode
            deviceDiagInput.deviceAttr = deviceAttr
            deviceDiagInput.deviceId = diagnostics.imei
            deviceDiagInput.deviceReview = diagnostics.reviewDetails
            deviceDiagInput.newPrice = tradeInParams.newPrice
            deviceDiagInput.oldPrice = diagnostics.tradeInPrice
            deviceDiagInput.tradeInType = tradeInType
            variables["params"] = deviceDiagInput
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return variables
    }

    private fun getQuery(): String {
        return GqlInsertDeviceDiag.GQL_QUERY
    }

    suspend fun processMessage(tradeInParams: TradeInParams, diagnostics: DeviceDiagnostics): DeviceDiagInputResponse{
        val variables = createRequestParams(tradeInParams, diagnostics)
        return repository.getGQLData(getQuery(), DeviceDiagInputResponse::class.java, variables) as DeviceDiagInputResponse
    }
}