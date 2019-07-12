package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.data.model.request.AddToCartRequest
import com.tokopedia.atc_common.domain.model.response.AddToCartGqlResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * Created by Irfan Khoirul on 2019-07-10.
 * How to use :
 *  - Set queryString
 *  - Set params
 */

class AddToCartUseCase @Inject constructor(private val queryString: String) : GraphqlUseCase() {

    companion object {
        const val PARAM_PRODUCT_ID = "#productID"
        const val PARAM_SHOP_ID = "#shopID"
        const val PARAM_QUANTITY = "#quantity"
        const val PARAM_NOTES = "#notes"
        const val PARAM_LANG = "#lang"
        const val PARAM_ATTRIBUTION = "#attribution"
        const val PARAM_LIST_TRACKER = "#listTracker"
        const val PARAM_UC_PARAMS = "#ucParams"
        const val PARAM_WAREHOUSE_ID = "#warehouseID"
        const val PARAM_ATC_FROM_EXTERNAL_SOURCE = "#atcFromExternalSource"
        const val PARAM_IS_SCP = "#isSCP"
    }

    fun setParams(addToCartRequest: AddToCartRequest) {
        queryString = queryString.replace(PARAM_PRODUCT_ID, addToCartRequest.productId.toString())
        queryString = queryString.replace(PARAM_SHOP_ID, addToCartRequest.shopId.toString())
        queryString = queryString.replace(PARAM_QUANTITY, addToCartRequest.quantity.toString())
        queryString = queryString.replace(PARAM_NOTES, addToCartRequest.notes)
        queryString = queryString.replace(PARAM_LANG, addToCartRequest.lang)
        queryString = queryString.replace(PARAM_ATTRIBUTION, addToCartRequest.attribution)
        queryString = queryString.replace(PARAM_LIST_TRACKER, addToCartRequest.listTracker)
        queryString = queryString.replace(PARAM_UC_PARAMS, addToCartRequest.ucParams)
        queryString = queryString.replace(PARAM_WAREHOUSE_ID, addToCartRequest.warehouseId.toString())
        queryString = queryString.replace(PARAM_ATC_FROM_EXTERNAL_SOURCE, addToCartRequest.atcFromExternalSource)
        queryString = queryString.replace(PARAM_IS_SCP, addToCartRequest.isSCP.toString())
    }

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        val graphqlRequest = GraphqlRequest(queryString, AddToCartGqlResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        return super.createObservable(params)
    }

}