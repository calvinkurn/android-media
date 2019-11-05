package com.tokopedia.flight.bookingV3.viewmodel


import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.bookingV3.data.FlightBookingParam
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.bookingV3.data.FlightCartViewEntity
import com.tokopedia.flight.bookingV3.data.FlightPromoViewEntity
import com.tokopedia.flight.bookingV3.data.mapper.FlightBookingMapper
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
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
    val flightPassengersData = MutableLiveData<List<FlightBookingPassengerViewModel>>()
    val flightPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightOtherPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightCancelVoucherSuccess = MutableLiveData<Boolean>()

    val flightBookingParam = FlightBookingParam()

    var retryCount = 0


    fun getCart(rawQuery: String, cartId: String) {
        val params = mapOf(PARAM_CART_ID to cartId)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCart.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCart.Response>().flightCart

            if (data.cartData.id.isNotBlank()) {
                flightCartResult.value = Success(FlightBookingMapper.mapToFlightCartView(data))
                flightPromoResult.value = FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher)
                flightPassengersData.value = FlightBookingMapper.mapToFlightPassengerEntity(data.cartData.flight.adult,
                        data.cartData.flight.child, data.cartData.flight.infant)
                flightPriceData.value = data.cartData.flight.priceDetail
            } else {
                if (data.meta.needRefresh && data.meta.maxRetry >= retryCount) {
                    retryCount++
                    getCart(rawQuery, cartId)
                } else {
//                    flightCartResult.value = Fail()
                }
            }
        }) {
            //            flightCartResult.value = Fail(it)
            val gson = Gson()
            val data = gson.fromJson(rawQuery, FlightCart.Response::class.java).flightCart
            flightCartResult.value = Success(FlightBookingMapper.mapToFlightCartView(data))
            flightPromoResult.value = FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher)
            flightPassengersData.value = FlightBookingMapper.mapToFlightPassengerEntity(data.cartData.flight.adult,
                    data.cartData.flight.child, data.cartData.flight.infant)
            flightPriceData.value = data.cartData.flight.priceDetail
        }
    }

    fun updatePromoData(promoData: PromoData) {
        flightPromoResult.value?.let {
            it.promoData = promoData
            flightPromoResult.value = it
        }
    }

    fun onPassengerResultReceived(passengerViewModel: FlightBookingPassengerViewModel) {
        val passengerViewModels = flightPassengersData.value?.toMutableList()
        val indexPassenger = passengerViewModels?.indexOf(passengerViewModel) ?: -1
        if (indexPassenger != -1) {
            passengerViewModels?.set(indexPassenger, passengerViewModel)
            flightPassengersData.value = passengerViewModels
        }
        //calculatePriceHere
    }

    fun updateOtherPriceData(priceDetail: List<FlightCart.PriceDetail>) {
        flightOtherPriceData.value = priceDetail
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

    fun getLuggageViewModels(): List<FlightBookingAmenityMetaViewModel> {
        return if (flightCartResult.value is Success) (flightCartResult.value as Success<FlightCartViewEntity>).data.luggageModels
         else listOf()
    }

    fun getMealViewModels(): List<FlightBookingAmenityMetaViewModel> {
        return if (flightCartResult.value is Success) (flightCartResult.value as Success<FlightCartViewEntity>).data.mealModels
        else listOf()
    }

    fun onInsuranceChanges(insurance: FlightCart.Insurance, checked: Boolean) {

        val otherPrices = flightOtherPriceData.value?.toMutableList() ?: mutableListOf()
        if (checked) {
            val index = flightBookingParam.insurances.indexOf(insurance)
            if (index == -1) {
                flightBookingParam.insurances.add(insurance)
                otherPrices.add(FlightCart.PriceDetail(label = String.format("%s (x%d)", insurance.name, flightBookingParam.totalPassenger),
                        priceNumeric = insurance.totalPriceNumeric, price = FlightCurrencyFormatUtil.convertToIdrPrice(insurance.totalPriceNumeric),
                        priceDetailId = insurance.id))
            }
        } else {
            val index = flightBookingParam.insurances.indexOf(insurance)
            if (index != -1) {
                flightBookingParam.insurances.removeAt(index)
                for ((index, price) in otherPrices.withIndex()) {
                    if (price.priceDetailId.equals(insurance.id)) {
                        otherPrices.removeAt(index)
                        break
                    }
                }
            }
        }
        flightOtherPriceData.value = otherPrices
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