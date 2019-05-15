package com.tokopedia.hotel.booking.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.booking.data.model.CartDataParam
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

/**
 * @author by resakemal on 13/05/19
 */

class HotelBookingViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val hotelCartResult = MutableLiveData<Result<HotelCart>>()

    fun getCartData(rawQuery: String, cartId: String, dummy: String = "") {
        val requestParams = CartDataParam(cartId)
        val params = mapOf(PARAM_CART_PROPERTY to requestParams)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_CART, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCart>()

            hotelCartResult.value = Success(data)
        }) {
            it.printStackTrace()
            val gson = Gson()
            hotelCartResult.value = Success(gson.fromJson(dummy,
                    HotelCart::class.java))
        }
    }

    companion object {
        const val PARAM_CART_PROPERTY = "data"
        private val TYPE_HOTEL_CART = HotelCart::class.java
    }
}