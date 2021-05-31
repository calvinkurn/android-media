package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.AtcConstant.ATC_ERROR_GLOBAL
import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_EXTERNAL
import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartExternalGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartExternalDataMapper
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartExternalUseCase @Inject constructor(@Named(MUTATION_ATC_EXTERNAL) private val queryString: String,
                                                   private val graphqlUseCase: GraphqlUseCase,
                                                   private val addToCartDataMapper: AddToCartExternalDataMapper,
                                                   private val analytics: AddToCartExternalAnalytics,
                                                   private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper) : UseCase<AddToCartExternalModel>() {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_USER_ID = "userID"
    }

    private fun getParams(productId: Long): Map<String, Any?> {
        return mapOf(
                PARAM_PRODUCT_ID to productId,
                KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
        )
    }

    override fun createObservable(requestParams: RequestParams): Observable<AddToCartExternalModel> {
        val productId = requestParams.getLong(PARAM_PRODUCT_ID, 0)
        val graphqlRequest = GraphqlRequest(queryString, AddToCartExternalGqlResponse::class.java, getParams(productId))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val response = it.getData<AddToCartExternalGqlResponse>(AddToCartExternalGqlResponse::class.java)
            if (response != null && response.response.status.equals("OK", true)) {
                val result = addToCartDataMapper.map(response)
                if (result.success == 1) {
                    val data = result.data
                    analytics.sendEnhancedEcommerceTracking(data)
                    AddToCartBaseAnalytics.sendAppsFlyerTracking(data.productId.toString(), data.productName, data.price.toString(),
                            data.quantity.toString(), data.category)
                    AddToCartBaseAnalytics.sendBranchIoTracking(data.productId.toString(), data.productName, data.price.toString(),
                            data.quantity.toString(), data.category, "",
                            "", "", "",
                            "", "", requestParams.getString(PARAM_USER_ID, ""))
                    result
                } else {
                    val message = response.response.data.message.firstOrNull() ?: ATC_ERROR_GLOBAL
                    throw MessageErrorException(message)
                }
            } else {
                throw MessageErrorException(ATC_ERROR_GLOBAL)
            }
        }
    }

}