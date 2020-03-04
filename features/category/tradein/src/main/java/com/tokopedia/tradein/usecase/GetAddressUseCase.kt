package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.AddressResult
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val repository: TradeInRepository){

    suspend fun getAddress(): AddressResult {
        val response = repository.getGQLData(getQuery(), ResponseData::class.java, createRequestParams()) as ResponseData
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

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tradein_address_corner)
    }

    fun createRequestParams(): Map<String, Any> {
        return mapOf("is_default" to 1,
                "limit" to 1,
                "page" to 1,
                "show_corner" to false,
                "show_address" to true)
    }
}