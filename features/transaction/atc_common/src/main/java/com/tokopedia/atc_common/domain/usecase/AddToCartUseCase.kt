package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.data.model.request.AddToCartRequest
import com.tokopedia.atc_common.domain.model.response.AddToCartGqlResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-07-10.
 * Don't forget to set params
 */

class AddToCartUseCase @Inject constructor(private val queryString: String) : GraphqlUseCase() {

    lateinit var stringQueryBuilder: StringBuilder

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
        stringQueryBuilder = StringBuilder(queryString)
        stringQueryBuilder = replaceParam(PARAM_PRODUCT_ID, addToCartRequest.productId.toString())
        stringQueryBuilder = replaceParam(PARAM_SHOP_ID, addToCartRequest.shopId.toString())
        stringQueryBuilder = replaceParam(PARAM_QUANTITY, addToCartRequest.quantity.toString())
        stringQueryBuilder = replaceParam(PARAM_NOTES, addToCartRequest.notes)
        stringQueryBuilder = replaceParam(PARAM_LANG, addToCartRequest.lang)
        stringQueryBuilder = replaceParam(PARAM_ATTRIBUTION, addToCartRequest.attribution)
        stringQueryBuilder = replaceParam(PARAM_LIST_TRACKER, addToCartRequest.listTracker)
        stringQueryBuilder = replaceParam(PARAM_UC_PARAMS, addToCartRequest.ucParams)
        stringQueryBuilder = replaceParam(PARAM_WAREHOUSE_ID, addToCartRequest.warehouseId.toString())
        stringQueryBuilder = replaceParam(PARAM_ATC_FROM_EXTERNAL_SOURCE, addToCartRequest.atcFromExternalSource)
        stringQueryBuilder = replaceParam(PARAM_IS_SCP, addToCartRequest.isSCP.toString())
    }

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        val graphqlRequest = GraphqlRequest(stringQueryBuilder.toString(), AddToCartGqlResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        return super.createObservable(params)
    }

    fun replaceParam(key: String, value: String): StringBuilder {
        return stringQueryBuilder.replace(stringQueryBuilder.indexOf(key), stringQueryBuilder.indexOf(key) + key.length, value)
    }

}