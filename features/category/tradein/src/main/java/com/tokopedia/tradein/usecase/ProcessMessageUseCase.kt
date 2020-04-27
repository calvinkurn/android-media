package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.DeviceAttr
import com.tokopedia.tradein.model.DeviceDiagInput
import com.tokopedia.tradein.model.DeviceDiagInputResponse
import com.tokopedia.tradein.model.DeviceDiagnostics
import com.tokopedia.tradein.repository.TradeInRepository
import com.tokopedia.tradein.view.viewcontrollers.BaseTradeInActivity.TRADEIN_OFFLINE
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class ProcessMessageUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
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
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_insert_device_diag)
    }

    suspend fun processMessage(tradeInParams: TradeInParams, diagnostics: DeviceDiagnostics): DeviceDiagInputResponse{
        val variables = createRequestParams(tradeInParams, diagnostics)
        return repository.getGQLData(getQuery(), DeviceDiagInputResponse::class.java, variables) as DeviceDiagInputResponse
    }
}