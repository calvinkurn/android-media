package com.tokopedia.moneyin.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.moneyin.R
import com.tokopedia.moneyin.model.MoneyInCourierResponse.ResponseData
import com.tokopedia.moneyin.repository.MoneyInRepository
import javax.inject.Inject

class MoneyInCourierRatesUseCase@Inject constructor(
        private val repository: MoneyInRepository) {

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

    private fun getQuery(resources: Resources?): String {
        return GraphqlHelper.loadRawString(resources, R.raw.gql_courier_rates)
    }

    suspend fun getCourierRates(resources: Resources?, destination: String): ResponseData {
        return repository.getGQLData(getQuery(resources), ResponseData::class.java, createRequestParams(destination))
    }
}