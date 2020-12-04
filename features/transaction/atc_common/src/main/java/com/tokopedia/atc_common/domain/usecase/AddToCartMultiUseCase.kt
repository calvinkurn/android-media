package com.tokopedia.atc_common.domain.usecase

import com.google.gson.JsonArray
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
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

    suspend fun execute(userId: String, query: String, listParam: ArrayList<AddToCartMultiParam>): Result<AtcMultiData> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(AtcMultiData::class.java)
        useCase.setRequestParams(generateParam(listParam))

        return try {
            val atc = useCase.executeOnBackground()
            if (atc.atcMulti.buyAgainData.success == 1) {
                for (param in listParam) {
                    val productId = param.productId
                    val productName = param.productName
                    val quantity = param.qty
                    val productPrice = param.productPrice
                    val category = param.category
                    AddToCartBaseAnalytics.sendAppsFlyerTracking(productId.toString(), productName, productPrice.toString(),
                            quantity.toString(), category)
                    AddToCartBaseAnalytics.sendBranchIoTracking(productId.toString(), productName, productPrice.toString(),
                            quantity.toString(), category, "",
                            "", "", "",
                            "", "", userId)
                }
            }
            Success(atc)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(arrayAtcMultiParam: ArrayList<AddToCartMultiParam>): Map<String, Any?> {
        return mapOf(PARAM to arrayAtcMultiParam)
    }
}