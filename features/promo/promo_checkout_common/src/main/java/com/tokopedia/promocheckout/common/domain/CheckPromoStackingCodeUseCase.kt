package com.tokopedia.promocheckout.common.domain

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.common.data.entity.request.PromoStackingRequestData
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

    fun setParams(checkPromoFirstStepParam: CheckPromoFirstStepParam) {

//        val objProductDetail = JsonObject()
//        objProductDetail.addProperty(PRODUCT_ID, promoStackingRequestData.productId)
//        objProductDetail.addProperty(QUANTITY, promoStackingRequestData.qty)
//
//        val listProductDetails = JsonArray()
//        listProductDetails.add(objProductDetail)
//
//        val listPromoCodes = JsonArray()
//        for (promoMerchant: String in promoStackingRequestData.promoMerchantList) {
//            listPromoCodes.add(promoMerchant)
//        }
//
//        val objOrders = JsonObject()
//        objOrders.addProperty(SHOP_ID, promoStackingRequestData.shopId)
//        objOrders.addProperty(UNIQUE_ID, promoStackingRequestData.uniqueId)
//        objOrders.add(PRODUCT_DETAILS, listProductDetails)
//        objOrders.add(CODES, listPromoCodes)
//
//        val listOrders = JsonArray()
//        listOrders.add(objOrders)
//
//        val promoMerchantCodeArray = JsonArray()
//        promoMerchantCodeArray.add(promoStackingRequestData.promoGlobalCode)
//
//        val objPromo = JsonObject()
//        objPromo.add(CODES, promoMerchantCodeArray)
//        objPromo.addProperty(SKIP_APPLY, promoStackingRequestData.skipApply)
//        objPromo.addProperty(CART_TYPE, promoStackingRequestData.cartType)
//        objPromo.addProperty(IS_SUGGESTED, promoStackingRequestData.isSuggested)
//        objPromo.add(ORDERS, listOrders)
//
//        val objParams = JsonObject()
//        objParams.add(PROMO, objPromo)
//
//        variables[PARAMS] = objParams
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkPromoFirstStepParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables.put("param", jsonObjectCheckoutRequest)

    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.check_promo_code_promostacking), ResponseGetPromoStackFirst::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }
}