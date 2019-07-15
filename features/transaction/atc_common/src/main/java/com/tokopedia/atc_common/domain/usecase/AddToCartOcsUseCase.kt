package com.tokopedia.atc_common.domain.usecase

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartOcsGqlResponse
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

class AddToCartOcsUseCase @Inject constructor(private val queryString: String,
                                              private val gson: Gson,
                                              private val graphqlUseCase: GraphqlUseCase) : UseCase<AddToCartDataModel>() {

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
        val addToCartRequest = requestParams?.getObject(AddToCartOcsUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartOcsRequestParams
        val graphqlRequest = GraphqlRequest(queryString, AddToCartOcsGqlResponse::class.java, getParams(addToCartRequest))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartGqlResponse = it.getData<AddToCartOcsGqlResponse>(AddToCartOcsGqlResponse::class.java)

            val dataModel = DataModel()
            dataModel.success = addToCartGqlResponse.addToCartResponse.data.success
            dataModel.cartId = addToCartGqlResponse.addToCartResponse.data.cartId
            dataModel.productId = addToCartGqlResponse.addToCartResponse.data.productId
            dataModel.quantity = addToCartGqlResponse.addToCartResponse.data.quantity
            dataModel.notes = addToCartGqlResponse.addToCartResponse.data.notes
            dataModel.shopId = addToCartGqlResponse.addToCartResponse.data.shopId
            dataModel.customerId = addToCartGqlResponse.addToCartResponse.data.customerId
            dataModel.warehouseId = addToCartGqlResponse.addToCartResponse.data.warehouseId
            dataModel.trackerAttribution = addToCartGqlResponse.addToCartResponse.data.trackerAttribution
            dataModel.trackerListName = addToCartGqlResponse.addToCartResponse.data.trackerListName
            dataModel.ucUtParam = addToCartGqlResponse.addToCartResponse.data.ucUtParam
            dataModel.isTradeIn = addToCartGqlResponse.addToCartResponse.data.isTradeIn
            dataModel.message = addToCartGqlResponse.addToCartResponse.data.message

            val addToCartDataModel = AddToCartDataModel()
            addToCartDataModel.status = addToCartGqlResponse.addToCartResponse.status
            addToCartDataModel.errorMessage = addToCartGqlResponse.addToCartResponse.errorMessage
            addToCartDataModel.data = dataModel

            addToCartDataModel
        }

    }

}