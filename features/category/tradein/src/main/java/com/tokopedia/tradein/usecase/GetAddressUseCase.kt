package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.tradein.model.AddressResult
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData
import com.tokopedia.tradein.raw.GQL_KERO_GET_ADDRESS
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

@GqlQuery("GqlKeroGetAddress", GQL_KERO_GET_ADDRESS)
class GetAddressUseCase @Inject constructor(
        private val repository: TradeInRepository) {

    suspend fun getAddress(): AddressResult {
        val response = repository.getGQLData(GqlKeroGetAddress.GQL_QUERY, ResponseData::class.java, createRequestParams())
        var addressData: ResponseData.KeroGetAddress.Data? = null
        var token: Token? = null
        response.let {
            it.keroGetAddress.data.let { listAddress ->
                if (listAddress.isNotEmpty()) {
                    listAddress.forEach { moneyInKero ->
                        if (moneyInKero.isPrimary) {
                            addressData = moneyInKero
                            return@forEach
                        }
                    }
                }
                token = it.keroGetAddress.token
            }
        }
        return AddressResult(addressData, token)
    }

    fun createRequestParams(): Map<String, Any> {
        return mapOf("is_default" to 1,
                "limit" to 1,
                "page" to 1,
                "show_corner" to false,
                "show_address" to true)
    }
}