package com.tokopedia.promocheckout.common.domain

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.ResponseGetPromoStackFirst
import com.tokopedia.usecase.RequestParams
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
    }

    fun setParams(productId : Int, qty: Int, promoCode1: String, promoCode2: String, shopId: Int,
                  uniqueId: String, promoGlobalCode: String, skipApply: Int, isSuggested: Int) {

        val objProductDetail = JsonObject()
        objProductDetail.addProperty(PRODUCT_ID, productId)
        objProductDetail.addProperty(QUANTITY, qty)

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
        promoMerchantCodeArray.add(promoGlobalCode)

        val objPromo = JsonObject()
        objPromo.add(CODES, promoMerchantCodeArray)
        objPromo.addProperty(SKIP_APPLY, skipApply)
        objPromo.addProperty(IS_SUGGESTED, isSuggested)
        objPromo.add(ORDERS, listOrders)

        val objParams = JsonObject()
        objParams.add(PROMO, objPromo)

        variables[PARAMS] = objParams
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.check_promo_code_promostacking), ResponseGetPromoStackFirst::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }
}