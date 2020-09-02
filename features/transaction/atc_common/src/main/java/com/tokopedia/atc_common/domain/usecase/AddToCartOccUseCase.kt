package com.tokopedia.atc_common.domain.usecase

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_OCC
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartOccGqlResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartOccUseCase @Inject constructor(@Named(MUTATION_ATC_OCC) private val queryString: String,
                                              private val graphqlUseCase: GraphqlUseCase,
                                              private val addToCartDataMapper: AddToCartDataMapper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"

        private const val PARAM = "param"

        private const val AF_PARAM_CATEGORY = "category"
        private const val AF_PARAM_CONTENT_ID = "id"
        private const val AF_PARAM_CONTENT_QUANTITY = "quantity"
        private const val AF_VALUE_CONTENT_TYPE = "product"
        private const val AF_VALUE_CURRENCY = "IDR"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartOccRequestParams
        val graphqlRequest = GraphqlRequest(queryString, AddToCartOccGqlResponse::class.java, getParams(addToCartRequest))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOccGqlResponse = it.getData<AddToCartOccGqlResponse>(AddToCartOccGqlResponse::class.java)
            val result = addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
            sendAppsFlyerTracking(result, addToCartRequest)
            result
        }

    }

    private fun getParams(addToCartRequest: AddToCartOccRequestParams): Map<String, Any> {
        return mapOf(PARAM to addToCartRequest)
    }

    private fun sendAppsFlyerTracking(result: AddToCartDataModel, addToCartRequest: AddToCartOccRequestParams) {
        if (!result.isDataError()) {
            val jsonArrayAfContent = JSONArray()
                    .put(JSONObject()
                            .put(AF_PARAM_CONTENT_ID, addToCartRequest.productId.toString())
                            .put(AF_PARAM_CONTENT_QUANTITY, addToCartRequest.quantity))
            TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
                    mutableMapOf<String, Any>(
                            AFInAppEventParameterName.CONTENT_ID to addToCartRequest.productId,
                            AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                            AFInAppEventParameterName.DESCRIPTION to addToCartRequest.productName,
                            AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
                            AFInAppEventParameterName.QUANTITY to addToCartRequest.quantity,
                            AFInAppEventParameterName.PRICE to addToCartRequest.price.replace("[^0-9]".toRegex(), ""),
                            AF_PARAM_CATEGORY to addToCartRequest.category,
                            AFInAppEventParameterName.CONTENT to jsonArrayAfContent.toString()
                    )
            )
        }
    }

}