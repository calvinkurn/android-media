package com.tokopedia.atc_common.domain.usecase

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.response.ocs.AddToCartOcsGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

open class AddToCartOcsUseCase @Inject constructor(@Named("atcOcsMutation") private val queryString: String,
                                                   private val gson: Gson,
                                                   private val graphqlUseCase: GraphqlUseCase,
                                                   private val addToCartDataMapper: AddToCartDataMapper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"
    }

    private fun getParams(ocsRequestParams: AddToCartOcsRequestParams): HashMap<String, Any?> {
        val variables = HashMap<String, Any?>()
        val jsonTreeAtcRequest = gson.toJsonTree(ocsRequestParams)
        val jsonObjectAtcRequest = jsonTreeAtcRequest.asJsonObject
        variables["params"] = jsonObjectAtcRequest

        return variables
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartOcsRequestParams
        val graphqlRequest = GraphqlRequest(queryString, AddToCartOcsGqlResponse::class.java, getParams(addToCartRequest))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOcsGqlResponse = it.getData<AddToCartOcsGqlResponse>(AddToCartOcsGqlResponse::class.java)
            val result = addToCartDataMapper.mapAddToCartOcsResponse(addToCartOcsGqlResponse)
            if (!result.isStatusError()) {
                AddToCartBaseAnalytics.sendAppsFlyerTracking(addToCartRequest.productId.toString(), addToCartRequest.productName, addToCartRequest.price,
                        addToCartRequest.quantity.toString(), addToCartRequest.category)
                AddToCartBaseAnalytics.sendBranchIoTracking(addToCartRequest.productId.toString(), addToCartRequest.productName, addToCartRequest.price,
                        addToCartRequest.quantity.toString(), addToCartRequest.category, addToCartRequest.categoryLevel1Id,
                        addToCartRequest.categoryLevel1Name, addToCartRequest.categoryLevel2Id, addToCartRequest.categoryLevel2Name,
                        addToCartRequest.categoryLevel3Id, addToCartRequest.categoryLevel3Name, addToCartRequest.userId)
            }
            result
        }
    }
}