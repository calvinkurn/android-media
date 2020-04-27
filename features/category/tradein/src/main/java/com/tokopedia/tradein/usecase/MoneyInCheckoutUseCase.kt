package com.tokopedia.tradein.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInCheckoutMutationResponse.ResponseData
import com.tokopedia.tradein.model.MoneyInMutationRequest
import com.tokopedia.tradein.model.MoneyInMutationRequest.Cart
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

class MoneyInCheckoutUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val repository: TradeInRepository) {

    fun createRequestParams(hardwareId: String,
                            addressId: Int,
                            spId: Int,
                            pickupTimeStart: Int,
                            pickupTimeEnd: Int): Map<String, Any> {
        val request = HashMap<String, Any>()
        val cartList: ArrayList<Cart> = arrayListOf()
        val cartInfoList: ArrayList<Cart.CartInfo> = arrayListOf()
        val metadata = HashMap<String, String>()
        val fields: ArrayList<Cart.CartInfo.Field> = arrayListOf()
        fields.add(Cart.CartInfo.Field(
                "origin_address_id",
                addressId))
        fields.add(Cart.CartInfo.Field(
                "sp_id",
                spId))
        fields.add(Cart.CartInfo.Field(
                "pickup_time_start",
                pickupTimeStart))
        fields.add(Cart.CartInfo.Field(
                "pickup_time_end",
                pickupTimeEnd))
        cartInfoList.add(Cart.CartInfo(
                1,
                "shipping",
                fields,
                1))
        metadata["hardware_id"] = hardwareId
        cartList.add(Cart(
                2,
                cartInfoList
                , Gson().toJson(metadata)))
        val params = MoneyInMutationRequest(cartList)

        request["params"] = params
        return request
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_checkout_general)
    }

    suspend fun makeCheckoutMutation(request: Map<String, Any>): ResponseData {
        return repository.getGQLData(getQuery(), ResponseData::class.java, request) as ResponseData
    }
}