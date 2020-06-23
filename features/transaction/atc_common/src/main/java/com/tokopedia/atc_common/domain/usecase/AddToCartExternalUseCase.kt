package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.AtcConstant.ATC_ERROR_GLOBAL
import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_EXTERNAL
import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartExternalGqlResponse
import com.tokopedia.atc_common.domain.AddToCartResponseErrorException
import com.tokopedia.atc_common.domain.mapper.AddToCartExternalDataMapper
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartExternalUseCase @Inject constructor(@Named(MUTATION_ATC_EXTERNAL) private val queryString: String,
                                                   private val graphqlUseCase: GraphqlUseCase,
                                                   private val addToCartDataMapper: AddToCartExternalDataMapper) : UseCase<AddToCartExternalDataModel>() {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
    }

    override fun createObservable(requestParams: RequestParams): Observable<AddToCartExternalDataModel> {
        val params = requestParams.parameters
        val graphqlRequest = GraphqlRequest(queryString, AddToCartExternalGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val response = it.getData<AddToCartExternalGqlResponse>(AddToCartExternalGqlResponse::class.java)
            if (response != null) {
                val result = addToCartDataMapper.map(response)
                if (result.success == 1) {
                    // Todo : send EE tracking
                    // Todo : send appsflyer tracking
                    // Todo : send branch io tracking
                    result
                } else {
                    var message = ATC_ERROR_GLOBAL
                    if (response.response.data.message.isNotEmpty()) {
                        message = response.response.data.message.joinToString { ". " }
                    }
                    throw AddToCartResponseErrorException(message)
                }
            } else {
                throw AddToCartResponseErrorException(ATC_ERROR_GLOBAL)
            }
        }
    }

//    private fun sendAppsFlyerTracking(result: AddToCartDataModel, addToCartRequest: AddToCartRequestParams) {
//        if (!result.isDataError()) {
//            TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
//                    mutableMapOf(
//                            AFInAppEventParameterName.CONTENT_ID to addToCartRequest.productId.toString(),
//                            AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
//                            AFInAppEventParameterName.DESCRIPTION to addToCartRequest.productName,
//                            AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
//                            AFInAppEventParameterName.QUANTITY to addToCartRequest.quantity,
//                            AFInAppEventParameterName.PRICE to addToCartRequest.price.replace("[^0-9]".toRegex(), ""),
//                            AF_PARAM_CATEGORY to addToCartRequest.category,
//                            AFInAppEventParameterName.CONTENT to JSONArray().put(JSONObject().put(AF_PARAM_CONTENT_ID, addToCartRequest.productId.toString()).put(AF_PARAM_CONTENT_QUANTITY, addToCartRequest.quantity))
//                    )
//            )
//        }
//    }

}