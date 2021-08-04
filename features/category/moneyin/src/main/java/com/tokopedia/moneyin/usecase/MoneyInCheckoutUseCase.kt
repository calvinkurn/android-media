package com.tokopedia.moneyin.usecase

import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.moneyin.R
import com.tokopedia.moneyin.model.MoneyInCheckoutMutationResponse.ResponseData
import com.tokopedia.moneyin.model.MoneyInMutationRequest
import com.tokopedia.moneyin.model.MoneyInMutationRequest.Cart
import com.tokopedia.moneyin.repository.MoneyInRepository
import javax.inject.Inject

class MoneyInCheckoutUseCase @Inject constructor(
        private val repository: MoneyInRepository) {

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

    private fun getQuery(resources: Resources?): String {
        return GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_checkout_general)
    }

    suspend fun makeCheckoutMutation(resources: Resources?, request: Map<String, Any>): ResponseData {
        return repository.getGQLData(getQuery(resources), ResponseData::class.java, request)
    }
}