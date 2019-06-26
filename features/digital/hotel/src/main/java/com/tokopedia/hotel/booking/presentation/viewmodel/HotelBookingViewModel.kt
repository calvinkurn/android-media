package com.tokopedia.hotel.booking.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.booking.data.model.CartDataParam
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.data.model.HotelCheckoutParam
import com.tokopedia.hotel.booking.data.model.HotelCheckoutResponse
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by resakemal on 13/05/19
 */

class HotelBookingViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val hotelCartResult = MutableLiveData<Result<HotelCart>>()
    val hotelCheckoutResult = MutableLiveData<Result<HotelCheckoutResponse>>()

    fun getCartData(rawQuery: String, cartId: String) {
        val requestParams = CartDataParam(cartId)
        val params = mapOf(PARAM_CART_PROPERTY to requestParams)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_CART, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCart.Response>()

            hotelCartResult.value = Success(data.response)
        }) {
            hotelCartResult.value = Fail(it)
        }
    }

    fun checkoutCart(rawQuery: String, hotelCheckoutParam: HotelCheckoutParam) {
        val params = mapOf(PARAM_CART_PROPERTY to hotelCheckoutParam)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_CHECKOUT, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCheckoutResponse>()

            hotelCheckoutResult.value = Success(data)
        }) {
            it.printStackTrace()
            hotelCheckoutResult.value = Fail(it)
        }
    }

    companion object {
        const val PARAM_CART_PROPERTY = "data"
        private val TYPE_HOTEL_CART = HotelCart.Response::class.java
        private val TYPE_HOTEL_CHECKOUT = HotelCheckoutResponse::class.java
    }
}