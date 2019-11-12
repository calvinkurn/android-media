package com.tokopedia.flight.bookingV3.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.constant.FlightBookingPassenger
import com.tokopedia.flight.booking.data.cloud.entity.Amenity
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel
import com.tokopedia.flight.bookingV3.data.*
import com.tokopedia.flight.bookingV3.data.mapper.FlightBookingMapper
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

/**
 * @author by jessica on 2019-10-25
 */

class FlightBookingViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val flightCartResult = MutableLiveData<Result<FlightCartViewEntity>>() //journey, insurance option, luggage option and meal option
    val flightVerifyResult = MutableLiveData<Result<FlightVerify.FlightVerifyMetaAndData>>() //flightVerify
    val flightCheckoutResult = MutableLiveData<Result<FlightCheckoutData>>() //flightCheckout
    val flightPromoResult = MutableLiveData<FlightPromoViewEntity>() //promoData
    val profileResult = MutableLiveData<Result<ProfileInfo>>() //profileData from userSession
    val flightPassengersData = MutableLiveData<List<FlightBookingPassengerViewModel>>() //passengerData
    val flightCancelVoucherSuccess = MutableLiveData<Boolean>() //voucher cancel success

    //priceListData
    val flightPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightOtherPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightAmenityPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()

    //route for flightDetail
    var flightDetailViewModels: List<FlightDetailViewModel> = listOf()

    val flightBookingParam = FlightBookingParam()

    var retryCount = 0

    init {
        flightPriceData.value = listOf()
        flightOtherPriceData.value = listOf()
        flightAmenityPriceData.value = listOf()
    }

    fun getCart(rawQuery: String, cartId: String, dummy: String = "") {
        val params = mapOf(PARAM_CART_ID to cartId)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCart.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCart.Response>().flightCart

            if (data.cartData.id.isNotBlank()) {
                flightPromoResult.value = FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher)
                flightPassengersData.value = FlightBookingMapper.mapToFlightPassengerEntity(data.cartData.flight.adult,
                        data.cartData.flight.child, data.cartData.flight.infant)
                flightPriceData.value = data.cartData.flight.priceDetail
                flightDetailViewModels = FlightBookingMapper.mapToFlightDetail(data.cartData.flight, data.included)
                flightCartResult.value = Success(FlightBookingMapper.mapToFlightCartView(data))
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
            val data = gson.fromJson(dummy, FlightCart.Response::class.java).flightCart
            flightPromoResult.value = FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher)
            flightPassengersData.value = FlightBookingMapper.mapToFlightPassengerEntity(data.cartData.flight.adult,
                    data.cartData.flight.child, data.cartData.flight.infant)
            flightPriceData.value = data.cartData.flight.priceDetail
            flightDetailViewModels = FlightBookingMapper.mapToFlightDetail(data.cartData.flight, data.included)
            flightCartResult.value = Success(FlightBookingMapper.mapToFlightCartView(data))
        }
    }

    fun verifyCartData(query: String, totalPrice: Int, cartId: String, contactName: String,
                       contactEmail: String, contactPhone: String, contactCountry: String, dummy: String) {
        val bookingVerifyParam = createVerifyParam(totalPrice, cartId, contactName, contactEmail, contactPhone, contactCountry)
        val params = mapOf(PARAM_VERIFY_CART to bookingVerifyParam)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(query, FlightVerify.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightVerify.Response>().flightVerify

            flightVerifyResult.value = Success(data)
        }) {

            val gson = Gson()
            val data = gson.fromJson(dummy, FlightVerify.Response::class.java).flightVerify
            flightVerifyResult.value = Success(data)
//            flightVerifyResult.value = Fail(it)
        }
    }

    fun createVerifyParam(totalPrice: Int, cartId: String, contactName: String,
                          contactEmail: String, contactPhone: String, contactCountry: String): FlightVerifyParam {
        val flightVerifyParam = FlightVerifyParam()
        try {
            val cartItem = FlightVerifyParam.CartItem()
            cartItem.productId = 27
            cartItem.quantity = 1
            cartItem.configuration.price = totalPrice
            cartItem.metaData.cartId = cartId
            cartItem.metaData.contactName = contactName
            cartItem.metaData.email = contactEmail
            cartItem.metaData.phone = contactPhone
            cartItem.metaData.country = contactCountry
            cartItem.metaData.ipAddress = FlightRequestUtil.getLocalIpAddress()
            cartItem.metaData.userAgent = FlightRequestUtil.getUserAgentForApiCall()

            for (passenger in flightPassengersData.value as List) {
                val flightVerifyPassenger = FlightVerifyParam.Passenger()
                flightVerifyPassenger.type = passenger.type
                flightVerifyPassenger.title = passenger.passengerTitleId
                flightVerifyPassenger.firstName = passenger.passengerFirstName
                flightVerifyPassenger.lastName = passenger.passengerLastName
                flightVerifyPassenger.dob = passenger.passengerBirthdate
                flightVerifyPassenger.nationality = passenger.passportNationality?.countryId ?: ""
                flightVerifyPassenger.passportNumber = passenger.passportNumber ?: ""
                flightVerifyPassenger.passportCountry = passenger.passportIssuerCountry?.countryId ?: ""
                flightVerifyPassenger.passportExpire = passenger.passportExpiredDate ?: ""
                for (mealMeta in passenger.flightBookingMealMetaViewModels) {
                    for (meal in mealMeta.amenities) {
                        val amenity = FlightVerifyParam.Amenity()
                        amenity.journeyId = mealMeta.journeyId
                        amenity.departureAirportId = mealMeta.departureId
                        amenity.arrivalAirportId = mealMeta.arrivalId
                        amenity.type = Amenity.MEAL
                        amenity.key = mealMeta.key
                        amenity.itemId = meal.id
                        flightVerifyPassenger.amenities.add(amenity)
                    }
                }
                for (luggageMeta in passenger.flightBookingLuggageMetaViewModels) {
                    for (luggage in luggageMeta.amenities) {
                        val amenity = FlightVerifyParam.Amenity()
                        amenity.journeyId = luggageMeta.journeyId
                        amenity.departureAirportId = luggageMeta.departureId
                        amenity.arrivalAirportId = luggageMeta.arrivalId
                        amenity.type = Amenity.MEAL
                        amenity.key = luggageMeta.key
                        amenity.itemId = luggage.id
                        flightVerifyPassenger.amenities.add(amenity)
                    }
                }
                cartItem.metaData.passengers.add(flightVerifyPassenger)
            }

            for (insurance in flightOtherPriceData.value as List) {
                cartItem.metaData.insurances.add(insurance.priceDetailId)
            }


            flightVerifyParam.cartItems.add(cartItem)
            flightVerifyParam.promoCode = (flightPromoResult.value as FlightPromoViewEntity).promoData.promoCode
        } catch (e: Exception) {
            Log.d("ERRRORRRR", e.localizedMessage)
        }

        return flightVerifyParam
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
        addPassengerAmenitiesPrices()
    }

    fun onTravellerAsPassenger(isEnable: Boolean) {
        val userProfile = if (profileResult.value is Success) (profileResult.value as Success).data else ProfileInfo()
        val passengers = (flightPassengersData.value ?: listOf()).toMutableList()
        if (passengers.isNotEmpty() && userProfile.fullName.isNotEmpty()) {
            val passenger = FlightBookingPassengerViewModel()
            passenger.passengerLocalId = 1
            passenger.type = FlightBookingPassenger.ADULT
            passenger.flightBookingLuggageMetaViewModels = arrayListOf()
            passenger.flightBookingMealMetaViewModels = arrayListOf()
            if (isEnable) {
                passenger.headerTitle = userProfile.fullName
                passenger.passengerFirstName = userProfile.fullName
                passenger.passengerBirthdate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, userProfile.birthday))
            } else {
                passenger.headerTitle = String.format("Penumpang dewasa")
            }
            passengers[0] = passenger
            flightPassengersData.value = passengers
        }
    }

    fun addPassengerAmenitiesPrices() {
        val flightBookingPassengers = flightPassengersData.value ?: listOf()
        // amenities
        val meals = hashMapOf<String, Int>()
        val luggages = hashMapOf<String, Int>()

        for (flightPassengerViewModel in flightBookingPassengers) {
            for (flightBookingAmenityMetaViewModel in flightPassengerViewModel.flightBookingMealMetaViewModels) {
                for (flightBookingAmenityViewModel in flightBookingAmenityMetaViewModel.amenities) {
                    if (meals[flightBookingAmenityMetaViewModel.description] != null) {
                        var total = meals[flightBookingAmenityMetaViewModel.description]!!
                        total += flightBookingAmenityViewModel.priceNumeric
                        meals[flightBookingAmenityMetaViewModel.description] = total
                    } else {
                        meals[flightBookingAmenityMetaViewModel.description] = flightBookingAmenityViewModel.priceNumeric
                    }
                }
            }
            for (flightBookingLuggageMetaViewModel in flightPassengerViewModel.flightBookingLuggageMetaViewModels) {
                for (flightBookingLuggageViewModel in flightBookingLuggageMetaViewModel.amenities) {
                    if (luggages[flightBookingLuggageMetaViewModel.description] != null) {
                        var total = luggages[flightBookingLuggageMetaViewModel.description]!!
                        total += flightBookingLuggageViewModel.priceNumeric
                        luggages[flightBookingLuggageMetaViewModel.description] = total
                    } else {
                        luggages[flightBookingLuggageMetaViewModel.description] = flightBookingLuggageViewModel.priceNumeric
                    }
                }
            }
        }

        val prices = listOf<FlightCart.PriceDetail>().toMutableList()
        for ((key, value) in meals) {
            prices.add(FlightCart.PriceDetail(String.format("%s %s", "Makanan",
                    key), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
        }
        for ((key, value) in luggages) {
            prices.add(FlightCart.PriceDetail(String.format("%s %s", "Bagasi",
                    key), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))

        }
        flightAmenityPriceData.value = prices
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

    fun getRouteForFlightDetail(id: String): FlightDetailViewModel {
        for (item in flightDetailViewModels) {
            if (item.id == id) {
                return item
            }
        }
        return FlightDetailViewModel()
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
                otherPrices.add(FlightCart.PriceDetail(label = String.format("%s (x%d)", insurance.name, flightPassengersData.value?.size ?: 1),
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
        val PARAM_VERIFY_CART = "data"
    }

}