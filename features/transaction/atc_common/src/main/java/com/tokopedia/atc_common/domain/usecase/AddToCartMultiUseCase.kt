package com.tokopedia.atc_common.domain.usecase

import com.google.gson.JsonArray
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddToCartMultiUseCase @Inject constructor(private val useCase: GraphqlUseCase<AtcMultiData>) {

    companion object {
        private const val PARAM = "param"
    }

    suspend fun execute(userId: String, query: String, paramJsonArray: JsonArray): Result<AtcMultiData> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(AtcMultiData::class.java)
        useCase.setRequestParams(generateParam(paramJsonArray))

        return try {
            val atc = useCase.executeOnBackground()
            if (atc.atcMulti.buyAgainData.success == 1) {
                for (product in atc.atcMulti.buyAgainData.listProducts) {
                    AddToCartBaseAnalytics.sendAppsFlyerTracking(product.productId.toString(), "", "",
                            product.quantity.toString(), "")
                    AddToCartBaseAnalytics.sendBranchIoTracking(product.productId.toString(), "", "",
                            product.quantity.toString(), "", "",
                            "", "", "",
                            "", "", userId)
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