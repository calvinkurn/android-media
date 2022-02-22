package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInValidateImeiModel
import com.tokopedia.tradein.model.request.TradeInValidateImeiInput
import com.tokopedia.tradein.raw.GQL_VALIDATE_IMEI
import com.tokopedia.tradein.repository.TradeInRepository
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

@GqlQuery("GqlValidateImei", GQL_VALIDATE_IMEI)
class TradeInValidateImeiUseCase @Inject constructor(
    private val repository: TradeInRepository,
    private val userSession: UserSession
) {

    suspend fun validateImei(laku6DeviceModel: Laku6DeviceModel, imei: String): TradeInValidateImeiModel {
        return repository.getGQLData(
            GqlValidateImei.GQL_QUERY,
            TradeInValidateImeiModel::class.java,
            createRequestParams(laku6DeviceModel, imei)
        )
    }

    private fun createRequestParams(laku6DeviceModel: Laku6DeviceModel, imei: String): Map<String, Any> {
        return mapOf<String, Any>(
            TradeinConstants.UseCase.PARAM_INPUT to
                    TradeInValidateImeiInput(
                        sessionId = laku6DeviceModel.sessionId,
                        appDeviceId = userSession.deviceId,
                        traceId = laku6DeviceModel.traceId,
                        modelInfo = laku6DeviceModel.model,
                        imei = imei
                    )
        )
    }
}