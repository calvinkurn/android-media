package com.tokopedia.flight.bookingV3.viewmodel


import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 2019-10-25
 */

class FlightBookingViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 val dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    val flightCartResult = MutableLiveData<Result<FlightCart>>()

    fun getCart(rawQuery: String, cartId: String) {
        val params = mapOf(PARAM_CART_ID to cartId)
//        launchCatchError(block = {
//            val data = withContext(Dispatchers.Default) {
//                val graphqlRequest = GraphqlRequest(rawQuery, FlightCart.Response::class.java, params)
//                graphqlRepository.getReseponse(listOf(graphqlRequest))
//            }.getSuccessData<FlightCart.Response>().flightCart
//
//            flightCartResult.value = Success(data)
//        }) {
//            flightCartResult.value = Fail(it)
//        }

        val gson = Gson()
        flightCartResult.value = Success(gson.fromJson(rawQuery,
                FlightCart.Response::class.java).flightCart)
    }

    companion object {
        val PARAM_CART_ID = "cartID"
    }

}