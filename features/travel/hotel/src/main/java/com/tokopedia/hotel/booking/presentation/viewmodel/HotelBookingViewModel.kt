package com.tokopedia.hotel.booking.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.booking.data.model.*
import com.tokopedia.hotel.roomlist.util.HotelUtil
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.domain.GetContactListUseCase
import com.tokopedia.travel.passenger.domain.UpsertContactListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by resakemal on 13/05/19
 */

class HotelBookingViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                private val getContactListUseCase: GetContactListUseCase,
                                                private val upsertContactListUseCase: UpsertContactListUseCase,
                                                val dispatcher: TravelDispatcherProvider) : BaseViewModel(dispatcher.io()) {

    val contactListResult = MutableLiveData<List<TravelContactListModel.Contact>>()
    val hotelCartResult = MutableLiveData<Result<HotelCart.Response>>()
    val hotelCheckoutResult = MutableLiveData<Result<HotelCheckoutResponse>>()

    val tokopointSumCouponResult = MutableLiveData<String>()

    fun getContactList(query: String) {
        launch {
            var contacts = getContactListUseCase.execute(query = query,
                    product = GetContactListUseCase.PARAM_PRODUCT_HOTEL)
            contactListResult.postValue(contacts.map {
                if (it.fullName.isBlank()) {
                    it.fullName = "${it.firstName} ${it.lastName}"
                }
                return@map it
            }.toMutableList())
        }
    }

    fun updateContactList(query: String,
                          updatedContact: TravelUpsertContactModel.Contact) {
        launch {
            upsertContactListUseCase.execute(query,
                    TravelUpsertContactModel(updateLastUsedProduct = UpsertContactListUseCase.PARAM_TRAVEL_HOTEL,
                            contacts = listOf(updatedContact)))
        }
    }

    fun getCartData(rawQuery: String, cartId: String) {
        val requestParams = CartDataParam(cartId)
        val params = mapOf(PARAM_CART_PROPERTY to requestParams)
        launchCatchError(block = {
            val data = withContext(dispatcher.ui()) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_CART, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCart.Response>()
            hotelCartResult.postValue(Success(data))
        }) {
            hotelCartResult.postValue(Fail(it))
        }
    }

    fun checkoutCart(rawQuery: String, hotelCheckoutParam: HotelCheckoutParam) {

        hotelCheckoutParam.idempotencyKey = generateIdEmpotency(hotelCheckoutParam.cartId)
        val params = mapOf(PARAM_CART_PROPERTY to hotelCheckoutParam)
        launchCatchError(block = {
            val data = withContext(dispatcher.ui()) {
                val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_CHECKOUT, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCheckoutResponse.Response>()

            hotelCheckoutResult.postValue(Success(data.response))
        }) {
            hotelCheckoutResult.postValue(Fail(it))
        }
    }

    fun getTokopointsSumCoupon(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(dispatcher.ui()) {
                val graphqlRequest = GraphqlRequest(rawQuery, TokopointsSumCoupon.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TokopointsSumCoupon.Response>()
            tokopointSumCouponResult.postValue(data.response.sumCouponUnitOpt)
        }) {
            tokopointSumCouponResult.postValue("")
        }
    }

    fun onCancelAppliedVoucher(rawQuery: String) {
        launchCatchError(block = {
            withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCancelVoucher.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCancelVoucher>()
        }) {
            it.printStackTrace()
        }
    }

    private fun generateIdEmpotency(cartId: String): String {
        val timeMillis = System.currentTimeMillis().toString()
        val token = HotelUtil.md5(timeMillis)
        return if (token.isEmpty()) "${cartId}_$timeMillis" else "${cartId}_$token"
    }

    companion object {
        const val PARAM_CART_PROPERTY = "data"
        private val TYPE_HOTEL_CART = HotelCart.Response::class.java
        private val TYPE_HOTEL_CHECKOUT = HotelCheckoutResponse.Response::class.java
    }
}