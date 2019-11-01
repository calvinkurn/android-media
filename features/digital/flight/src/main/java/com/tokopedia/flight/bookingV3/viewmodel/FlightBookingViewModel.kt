package com.tokopedia.flight.bookingV3.viewmodel


import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.bookingV3.data.FlightCartViewEntity
import com.tokopedia.flight.bookingV3.data.FlightPromoViewEntity
import com.tokopedia.flight.bookingV3.data.mapper.FlightBookingMapper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
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
                                                 val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val flightCartResult = MutableLiveData<Result<FlightCartViewEntity>>()
    val flightPromoResult = MutableLiveData<FlightPromoViewEntity>()
    val profileResult = MutableLiveData<Result<ProfileInfo>>()
    val flightCancelVoucherSuccess = MutableLiveData<Boolean>()


    fun getCart(rawQuery: String, cartId: String) {
        val params = mapOf(PARAM_CART_ID to cartId)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCart.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCart.Response>().flightCart

            flightCartResult.value = Success(FlightBookingMapper.mapToFlightCartView(data))
            flightPromoResult.value = FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher)
        }) {
            //            flightCartResult.value = Fail(it)
            val gson = Gson()
            val data = gson.fromJson(rawQuery, FlightCart.Response::class.java).flightCart
            flightCartResult.value = Success(FlightBookingMapper.mapToFlightCartView(data))
            flightPromoResult.value = FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher)
        }
    }

    fun updatePromoData(promoData: PromoData) {
        flightPromoResult.value?.let {
            it.promoData = promoData
            flightPromoResult.value = it
        }
    }


    fun getProfile(rawQuery: String) {
        launchCatchError(block = {
            val profileInfo = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, ProfilePojo::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<ProfilePojo>().profileInfo
            profileInfo.phone = transformPhoneNum(profileInfo.phone)

            profileResult.value = Success(profileInfo)
        })
        {
            profileResult.value = Fail(it)
        }
    }

    fun onCancelAppliedVoucher(rawQuery: String) {
        launchCatchError(block = {
            val profileInfo = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCancelVoucher.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCancelVoucher>()

            flightCancelVoucherSuccess.value = true
        })
        {
            flightCancelVoucherSuccess.value = false
        }
    }

    private fun transformPhoneNum(phoneRawString: String): String {
        var phoneString: String = checkStart(phoneRawString)
        phoneString = phoneString.replace("-", "")
        val phoneNumArr = StringBuilder(phoneString)
        if (phoneNumArr.isNotEmpty()) {
            phoneNumArr.replace(0, 1, "")
        }
        return phoneNumArr.toString()
    }

    private fun checkStart(phoneNumber: String): String {
        var phoneRawString = phoneNumber
        if (phoneRawString.startsWith("62")) {
            phoneRawString = phoneRawString.replaceFirst("62".toRegex(), "0")
        } else if (phoneRawString.startsWith("+62")) {
            phoneRawString = phoneRawString.replaceFirst("\\+62".toRegex(), "0")
        }
        return phoneRawString
    }

    companion object {
        val PARAM_CART_ID = "cartID"
    }

}