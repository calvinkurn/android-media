package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

class MoneyInCourierRatesUseCase@Inject constructor(
        @ApplicationContext private val context: Context,
        private val repository: TradeInRepository) {

    fun createRequestParams(destination: String): Map<String, Any> {
        val request = HashMap<String, Any>()
        val input = HashMap<String, String>()
        input["features"] = "money_in"
        input["weight"] = "1"
        input["destination"] = destination
        input["from"] = "client"
        input["type"] = "android"
        input["lang"] = "en"
        request["input"] = input
        return request
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_courier_rates)
    }

    suspend fun getCourierRates(destination: String): ResponseData {
        return repository.getGQLData(getQuery(), ResponseData::class.java, createRequestParams(destination)) as ResponseData
    }
}