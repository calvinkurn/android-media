package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddToCartMultiUseCase @Inject constructor(private val useCase: GraphqlUseCase<AtcMultiData>,
                                                private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper) {

    companion object {
        private const val PARAM = "param"
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
        return mapOf(
                PARAM to arrayAtcMultiParam,
                KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
        )
    }
}