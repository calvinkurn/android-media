package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tradein.TradeinConstants.UseCase.PARAM_INPUT
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.tradein.model.TradeInDetailModel
import com.tokopedia.tradein.model.request.GetTradeInDetailInput
import com.tokopedia.tradein.raw.GQL_TRADE_IN_DETAIL
import com.tokopedia.tradein.repository.TradeInRepository
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

@GqlQuery("GqlTradeInDetail", GQL_TRADE_IN_DETAIL)
class TradeInDetailUseCase @Inject constructor(
    private val repository: TradeInRepository,
    private val userSession: UserSession
) {

    suspend fun getTradeInDetail(
        laku6DeviceModel: Laku6DeviceModel,
        productPrice: Int,
        userAddressData: LocalCacheModel
    ): TradeInDetailModel {
        return repository.getGQLData(GqlTradeInDetail.GQL_QUERY, TradeInDetailModel::class.java, createRequestParams(laku6DeviceModel, productPrice, userAddressData))
    }

    fun createRequestParams(
        laku6DeviceModel: Laku6DeviceModel,
        productPrice: Int,
        userAddressData: LocalCacheModel
    ): Map<String, Any> {
        return mapOf<String,Any>(PARAM_INPUT to
                GetTradeInDetailInput(
                    sessionId = laku6DeviceModel.sessionId,
                    appDeviceId = userSession.deviceId,
                    traceId = laku6DeviceModel.traceId,
                    uniqueCode = "",
                    modelInfo = laku6DeviceModel.model,
                    originalPrice = productPrice,
                    userId = userSession.userId,
                    userLocation = GetTradeInDetailInput.UserLocation(
                        districtId = userAddressData.district_id,
                        cityId = "1"/*userAddressData.city_id*/,
                        latitude = userAddressData.lat,
                        longitude = userAddressData.long,
                        postalCode = userAddressData.postal_code
                    )
                ))
    }
}