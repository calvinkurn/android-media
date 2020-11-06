package com.tokopedia.atc_common.domain.usecase

import com.google.gson.JsonArray
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

class AddToCartMultiUseCase @Inject constructor(private val useCase: GraphqlUseCase<AtcMultiData>) {

    companion object {
        private const val PARAM = "param"

        private const val PRODUCT_ID_KEY = "product_id"
        private const val PRODUCT_NAME_KEY = "product_name"
        private const val QUANTITY_KEY = "quantity"
        private const val PRODUCT_PRICE_KEY = "product_price"
        private const val CATEGORY_KEY = "category"
    }

    suspend fun execute(userId: String, query: String, paramJsonArray: JsonArray): Result<AtcMultiData> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(AtcMultiData::class.java)
        useCase.setRequestParams(generateParam(paramJsonArray))

        return try {
            val atc = useCase.executeOnBackground()
            if (atc.atcMulti.buyAgainData.success == 1) {
                for (jsonElement in paramJsonArray) {
                    try {
                        val product = jsonElement.asJsonObject
                        val productId = product[PRODUCT_ID_KEY].asInt
                        val productName = product[PRODUCT_NAME_KEY].asString
                        val quantity = product[QUANTITY_KEY].asInt
                        val productPrice = product[PRODUCT_PRICE_KEY].asInt
                        val category = product[CATEGORY_KEY]?.asString ?: ""
                        AddToCartBaseAnalytics.sendAppsFlyerTracking(productId.toString(), productName, productPrice.toString(),
                                quantity.toString(), category)
                        AddToCartBaseAnalytics.sendBranchIoTracking(productId.toString(), productName, productPrice.toString(),
                                quantity.toString(), category, "",
                                "", "", "",
                                "", "", userId)
                    } catch (t: Throwable) {
                        // failed parse json
                        Timber.d(t)
                    }
                }
            }
            Success(atc)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(jsonArray: JsonArray): Map<String, Any?> {
        return mapOf(PARAM to jsonArray)
    }
}