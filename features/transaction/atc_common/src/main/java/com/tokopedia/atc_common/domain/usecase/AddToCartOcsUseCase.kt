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
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

class AddToCartOcsUseCase @Inject constructor(@Named("atcOcsMutation") private val queryString: String,
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
            addToCartGqlResponse.let {
                val dataModel = DataModel()
                dataModel.success = it.addToCartResponse.data.success
                dataModel.cartId = it.addToCartResponse.data.cartId
                dataModel.productId = it.addToCartResponse.data.productId
                dataModel.quantity = it.addToCartResponse.data.quantity
                dataModel.notes = it.addToCartResponse.data.notes
                dataModel.shopId = it.addToCartResponse.data.shopId
                dataModel.customerId = it.addToCartResponse.data.customerId
                dataModel.warehouseId = it.addToCartResponse.data.warehouseId
                dataModel.trackerAttribution = it.addToCartResponse.data.trackerAttribution
                dataModel.trackerListName = it.addToCartResponse.data.trackerListName
                dataModel.ucUtParam = it.addToCartResponse.data.ucUtParam
                dataModel.isTradeIn = it.addToCartResponse.data.isTradeIn
                dataModel.message = it.addToCartResponse.data.message

                val addToCartDataModel = AddToCartDataModel()
                addToCartDataModel.status = it.addToCartResponse.status
                addToCartDataModel.errorMessage = it.addToCartResponse.errorMessage
                addToCartDataModel.data = dataModel

                addToCartDataModel
            }
        }

    }

}