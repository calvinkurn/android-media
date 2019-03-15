package com.tokopedia.promocheckout.common.domain

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.Response
import com.tokopedia.promocheckout.common.view.uimodel.ResponseUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

class CheckPromoStackingCodeUseCase @Inject constructor (@ApplicationContext private val context: Context)
    : GraphqlUseCase() {

    val variables = HashMap<String, Any?>()

    companion object {
        private const val PARAMS = "params"
        private const val PROMO = "promo"
        private const val PRODUCT_ID = "product_id"
        private const val QUANTITY = "quantity"
        private const val SHOP_ID = "shop_id"
        private const val UNIQUE_ID = "unique_id"
        private const val PRODUCT_DETAILS = "product_details"
        private const val CODES = "codes"
        private const val SKIP_APPLY = "skip_apply"
        private const val IS_SUGGESTED = "is_suggested"
        private const val CART_TYPE = "cart_type"
        private const val ORDERS = "orders"

        @JvmOverloads
        fun createRequestParams(): RequestParams? {
            val requestParams = RequestParams.create()

            val productId = 123
            val quantity = 1
            val promoCode1 = "VOUCHERTOKO10"
            val promoCode2 = "JNE100"
            val shopId = 1
            // val uniqueId = ""
            val promoMerchantCode = "CASHBACK50"
            val skipApply = 0
            val isSuggested = 1
            val cartType = "default"

            val objProductDetail = JsonObject()
            objProductDetail.addProperty(PRODUCT_ID, productId)
            objProductDetail.addProperty(QUANTITY, quantity)

            val listProductDetails = JsonArray()
            listProductDetails.add(objProductDetail)

            val listPromoCodes = JsonArray()
            listPromoCodes.add(promoCode1)
            listPromoCodes.add(promoCode2)

            val objOrders = JsonObject()
            // objOrders.addProperty(SHOP_ID, shopId)
            objOrders.addProperty(UNIQUE_ID, "")
            objOrders.add(PRODUCT_DETAILS, listProductDetails)
            objOrders.add(CODES, listPromoCodes)

            val listOrders = JsonArray()
            listOrders.add(objOrders)

            val promoMerchantCodeArray = JsonArray()
            promoMerchantCodeArray.add(promoMerchantCode)

            val objPromo = JsonObject()
            objPromo.add(CODES, promoMerchantCodeArray)
            objPromo.addProperty(SKIP_APPLY, skipApply)
            objPromo.addProperty(IS_SUGGESTED, isSuggested)
            objPromo.addProperty(CART_TYPE, cartType)
            objPromo.add(ORDERS, listOrders)

            val objParam = JsonObject()
            objParam.add(PROMO, objPromo)

            requestParams.putObject(PARAMS, objParam)
            return requestParams
        }
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.check_promo_code_promostacking), Response::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

    fun setParams() {
        // val requestParams = RequestParams.create()

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
        objProductDetail.addProperty(PRODUCT_ID, productId)
        objProductDetail.addProperty(QUANTITY, quantity)

        val listProductDetails = JsonArray()
        listProductDetails.add(objProductDetail)

        val listPromoCodes = JsonArray()
        listPromoCodes.add(promoCode1)
        listPromoCodes.add(promoCode2)

        val objOrders = JsonObject()
        objOrders.addProperty(SHOP_ID, shopId)
        objOrders.addProperty(UNIQUE_ID, uniqueId)
        objOrders.add(PRODUCT_DETAILS, listProductDetails)
        objOrders.add(CODES, listPromoCodes)

        val listOrders = JsonArray()
        listOrders.add(objOrders)

        val promoMerchantCodeArray = JsonArray()
        promoMerchantCodeArray.add(promoMerchantCode)

        val objPromo = JsonObject()
        objPromo.add(CODES, promoMerchantCodeArray)
        objPromo.addProperty(SKIP_APPLY, skipApply)
        objPromo.addProperty(IS_SUGGESTED, isSuggested)
        objPromo.add(ORDERS, listOrders)

        variables[PARAMS] = objPromo
    }
}