package com.tokopedia.promocheckout.common.domain

import android.content.Context
import android.content.res.Resources
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.Data
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.DataResponseFirstStep
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Subscriber
import java.util.*

open class CheckPromoStackingCodeUseCase(val context: Context) : GraphqlUseCase() {

    lateinit var variables: HashMap<String, Any?>

    private object PARAM {
        internal val PRODUCT_ID = "product_id"
        internal val QUANTITY = "quantity"
        internal val SHOP_ID = "shop_id"
        internal val UNIQUE_ID = "unique_id"
        internal val PRODUCT_DETAILS = "product_details"
        internal val CODES = "codes"
        internal val SKIP_APPLY = "skip_apply"
        internal val IS_SUGGESTED = "is_suggested"
        internal val ORDERS = "orders"
        internal val PARAMS = "params"
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.check_promo_code_promostacking), DataResponseFirstStep::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

    override fun createObservable(requestParams: RequestParams): Observable<GraphqlResponse>? {
        val variables = HashMap<String, Any>()
        createRequestParams()

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.check_promo_code_promostacking), DataResponseFirstStep::class.java, variables)

        clearRequest()
        addRequest(graphqlRequest)
        return createObservable(RequestParams.EMPTY)?.map {
            it
        }
    }

    /*override fun createObservable(requestParams: RequestParams?): Observable<Data> {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        createRequestParams()

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.check_promo_code_promostacking), DataResponseFirstStep::class.java, variables)
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .flatMap {
                    val checkFirstStep = it.getData<DataResponseFirstStep>(DataResponseFirstStep::class.java)
                    if(checkFirstStep?.getPromoStackFirst?.status.equals("false", true)){
                        throw CheckPromoCodeException("Tidak berhasil check promo stacking code!!")
                    }
                    Observable.just(checkFirstStep?.getPromoStackFirst?.data)
                }
    }*/

    fun createRequestParams(): RequestParams? {
        val requestParams = RequestParams.create()
        variables = HashMap()

        val productId = 123
        val quantity = 1
        val promoCode1 = "VOUCHERTOKO10"
        val promoCode2 = "JNE100"
        val shopId = 1
        val uniqueId = ""
        val promoMerchantCode = "CASHBACK50"
        val skipApply = 0
        val isSuggested = 1

        val objProductDetail = JsonObject()
        objProductDetail.addProperty(PARAM.PRODUCT_ID, productId)
        objProductDetail.addProperty(PARAM.QUANTITY, quantity)

        val listProductDetails = JsonArray()
        listProductDetails.add(objProductDetail)

        val listPromoCodes = JsonArray()
        listPromoCodes.add(promoCode1)
        listPromoCodes.add(promoCode2)

        val objOrders = JsonObject()
        objOrders.addProperty(PARAM.SHOP_ID, shopId)
        objOrders.addProperty(PARAM.UNIQUE_ID, uniqueId)
        objOrders.add(PARAM.PRODUCT_DETAILS, listProductDetails)
        objOrders.add(PARAM.CODES, listPromoCodes)

        val listOrders = JsonArray()
        listOrders.add(objOrders)

        val promoMerchantCodeArray = JsonArray()
        promoMerchantCodeArray.add(promoMerchantCode)

        val objPromo = JsonObject()
        objPromo.add(PARAM.CODES, promoMerchantCodeArray)
        objPromo.addProperty(PARAM.SKIP_APPLY, skipApply)
        objPromo.addProperty(PARAM.IS_SUGGESTED, isSuggested)
        objPromo.add(PARAM.ORDERS, listOrders)

        variables[PARAM.PARAMS] = objPromo
        requestParams.putObject(PARAM.PARAMS, objPromo)
        return requestParams
    }
}