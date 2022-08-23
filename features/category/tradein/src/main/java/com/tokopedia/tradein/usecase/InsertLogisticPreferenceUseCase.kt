package com.tokopedia.tradein.usecase

import com.tokopedia.common_tradein.model.DeviceDiagnostics
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.TradeinConstants.UseCase.PARAM_INPUT
import com.tokopedia.tradein.model.InsertTradeInLogisticModel
import com.tokopedia.tradein.model.request.InsertTradeInLogisticPreferenceInput
import com.tokopedia.tradein.raw.GQL_INSERT_LOGISTIC_PREFERENCE
import com.tokopedia.tradein.repository.TradeInRepository
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

@GqlQuery("GqlInsertLogisticPreference", GQL_INSERT_LOGISTIC_PREFERENCE)
class InsertLogisticPreferenceUseCase @Inject constructor(
    private val repository: TradeInRepository,
    private val userSession: UserSession
) {

    suspend fun insertLogistic(
        is3PL: Boolean,
        finalPrice: Double,
        tradeInPrice: Double,
        imei: String,
        campaignTagId: String,
        diagnosticsData: DeviceDiagnostics
    ): InsertTradeInLogisticModel {
        return repository.getGQLData(
            GqlInsertLogisticPreference.GQL_QUERY, InsertTradeInLogisticModel::class.java,
            createRequestParams(
                is3PL = is3PL,
                finalPrice = finalPrice,
                tradeInPrice = tradeInPrice,
                imei = imei,
                diagnosticsData = diagnosticsData,
                campaignTagId = campaignTagId
            )
        )
    }

    private fun createRequestParams(
        is3PL: Boolean,
        finalPrice: Double,
        tradeInPrice: Double,
        imei: String,
        diagnosticsData: DeviceDiagnostics,
        campaignTagId: String
    ): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_INPUT to
                    InsertTradeInLogisticPreferenceInput(
                        deviceId = userSession.deviceId,
                        is3PL = is3PL,
                        finalPrice = finalPrice,
                        tradeInPrice = tradeInPrice,
                        imei = imei,
                        uniqueCode = diagnosticsData.tradeInUniqueCode ?: "",
                        campaignTagId = campaignTagId,
                        deviceAttr = InsertTradeInLogisticPreferenceInput.DeviceAttr(
                            brand = diagnosticsData.brand ?: "",
                            grade = diagnosticsData.grade ?: "",
                            imei = arrayListOf(imei),
                            modelId = diagnosticsData.modelId ?: 0,
                            model = diagnosticsData.model ?: "",
                            ram = diagnosticsData.ram ?: "",
                            storage = diagnosticsData.storage ?: ""
                        )
                    )
        )
    }
}