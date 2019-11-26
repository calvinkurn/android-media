package com.tokopedia.flight.bookingV3.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.constant.FlightBookingPassenger
import com.tokopedia.flight.booking.data.cloud.entity.Amenity
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.bookingV3.data.*
import com.tokopedia.flight.bookingV3.data.mapper.FlightBookingMapper
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.regex.Pattern
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
    val errorToastMessageData = MutableLiveData<Int>()

    //priceListData
    val flightPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightOtherPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightAmenityPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()

    //route for flightDetail
    var flightDetailViewModels: List<FlightDetailViewModel> = listOf()

    private val flightBookingParam = FlightBookingModel()

    var retryCount = 0
    var verifyRetryCount = 0
    var pastVerifyParam = ""

    init {
        flightPriceData.value = listOf()
        flightOtherPriceData.value = listOf()
        flightAmenityPriceData.value = listOf()
        flightPromoResult.value = FlightPromoViewEntity()
        flightPassengersData.value = listOf()
    }

    fun getSearchParam(): FlightSearchPassDataViewModel = flightBookingParam.searchParam
    fun getFlightPriceModel(): FlightPriceViewModel = flightBookingParam.flightPriceViewModel

    fun getCart(rawQuery: String, cartId: String, autoVerify: Boolean = false, bookingVerifyParam: FlightVerifyParam? = null, verifyQuery: String = "", checkVoucherQuery: String = "") {
        val params = mapOf(PARAM_CART_ID to cartId)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCart.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCart.Response>().flightCart

            if (data.cartData.id.isNotBlank() && !data.meta.needRefresh) {
                flightBookingParam.departureDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, data.cartData.flight.journeys[0].departureTime))
                flightBookingParam.isDomestic = data.cartData.flight.isDomestic
                flightBookingParam.isMandatoryDob = data.cartData.flight.mandatoryDob
                flightPromoResult.value = FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher)
                if (flightPassengersData.value?.isEmpty() != false) flightPassengersData.value = FlightBookingMapper.mapToFlightPassengerEntity(data.cartData.flight.adult,
                        data.cartData.flight.child, data.cartData.flight.infant)
                flightPriceData.value = data.cartData.flight.priceDetail
                flightDetailViewModels = FlightBookingMapper.mapToFlightDetail(data.cartData.flight, data.included)
                flightCartResult.value = Success(FlightBookingMapper.mapToFlightCartView(data))
                if (autoVerify && bookingVerifyParam != null && verifyQuery.isNotEmpty() && checkVoucherQuery.isNotEmpty())
                    verifyCartData(verifyQuery, bookingVerifyParam, checkVoucherQuery)
                retryCount = 0
            } else {
                if (data.meta.needRefresh && data.meta.maxRetry >= retryCount) {
                    retryCount++
                    delay(data.meta.refreshTime * 1000.toLong())
                    getCart(rawQuery, cartId)
                } else {
                    retryCount = 0
                    flightCartResult.value = Fail(MessageErrorException(FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY))
                }
            }
        }) {
            flightCartResult.value = Fail(it)
        }
    }

    fun flightIsDomestic(): Boolean = flightBookingParam.isDomestic
    fun getCartId(): String = flightBookingParam.cartId

    fun verifyCartData(query: String, bookingVerifyParam: FlightVerifyParam, checkVoucherQuery: String) {
        val promoCode = (flightPromoResult.value as FlightPromoViewEntity).promoData.promoCode

        if (pastVerifyParam.isEmpty() || generateVerifyParam(bookingVerifyParam).equals(pastVerifyParam)) {
            val params = mapOf(PARAM_VERIFY_CART to bookingVerifyParam)

            launchCatchError(block = {
                val data = async {
                    withContext(Dispatchers.Default) {
                        val graphqlRequest = GraphqlRequest(query, FlightVerify.Response::class.java, params)
                        graphqlRepository.getReseponse(listOf(graphqlRequest))
                    }.getSuccessData<FlightVerify.Response>().flightVerify
                }

                if (promoCode.isNotEmpty()) {
                    val checkPromoData = async { checkVoucher(checkVoucherQuery, getCartId()) }
                    val flightVerifyData = data.await()
                    flightVerifyData.data.cartItems[0].promoEligibility = checkPromoData.await()

                    if (!flightVerifyData.meta.needRefresh && flightVerifyData.data.cartItems.isNotEmpty()) {
                        verifyRetryCount = 0
                        flightVerifyResult.value = Success(flightVerifyData)
                        pastVerifyParam = generateVerifyParam(bookingVerifyParam)
                    } else {
                        if (flightVerifyData.meta.needRefresh && flightVerifyData.meta.maxRetry >= verifyRetryCount) {
                            verifyRetryCount++
                            delay(flightVerifyData.meta.refreshTime * 1000.toLong())
                            verifyCartData(query, bookingVerifyParam, checkVoucherQuery)
                        } else {
                            verifyRetryCount = 0
                            flightVerifyResult.value = Fail(MessageErrorException(FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY))
                        }
                    }
                } else {
                    val flightVerifyData = data.await()
                    if (!flightVerifyData.meta.needRefresh && flightVerifyData.data.cartItems.isNotEmpty()) {
                        verifyRetryCount = 0
                        flightVerifyResult.value = Success(flightVerifyData)
                        pastVerifyParam = generateVerifyParam(bookingVerifyParam)
                    } else {
                        if (flightVerifyData.meta.needRefresh && flightVerifyData.meta.maxRetry >= verifyRetryCount) {
                            verifyRetryCount++
                            delay(flightVerifyData.meta.refreshTime * 1000.toLong())
                            verifyCartData(query, bookingVerifyParam, checkVoucherQuery)
                        } else {
                            verifyRetryCount = 0
                            flightVerifyResult.value = Fail(MessageErrorException(FlightErrorConstant.FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY))
                        }
                    }
                }

            }) {
                flightVerifyResult.value = Fail(it)
            }
        }
    }

    fun verifyCartData(query: String, totalPrice: Int, contactName: String,
                       contactEmail: String, contactPhone: String, contactCountry: String,
                       checkVoucherQuery: String, addToCartQuery: String, idempotencyKey: String, getCartQuery: String) {

        if (validateFields(contactName, contactEmail, contactPhone)) {
            val promoCode = (flightPromoResult.value as FlightPromoViewEntity).promoData.promoCode
            val bookingVerifyParam = createVerifyParam(totalPrice, getCartId(), contactName, contactEmail, contactPhone, contactCountry)

            if (pastVerifyParam.isEmpty() || generateVerifyParam(bookingVerifyParam).equals(pastVerifyParam)) {
                val params = mapOf(PARAM_VERIFY_CART to bookingVerifyParam)

                launchCatchError(block = {
                    val data = async {
                        withContext(Dispatchers.Default) {
                            val graphqlRequest = GraphqlRequest(query, FlightVerify.Response::class.java, params)
                            graphqlRepository.getReseponse(listOf(graphqlRequest))
                        }.getSuccessData<FlightVerify.Response>().flightVerify
                    }

                    if (promoCode.isNotEmpty()) {
                        val checkPromoData = async { checkVoucher(checkVoucherQuery, getCartId()) }
                        val flightVerifyData = data.await()
                        flightVerifyData.data.cartItems[0].promoEligibility = checkPromoData.await()

                        if (!flightVerifyData.meta.needRefresh && flightVerifyData.data.cartItems.isNotEmpty()) {
                            verifyRetryCount = 0
                            flightVerifyResult.value = Success(flightVerifyData)
                            pastVerifyParam = generateVerifyParam(bookingVerifyParam)
                        } else {
                            if (flightVerifyData.meta.needRefresh && flightVerifyData.meta.maxRetry >= verifyRetryCount) {
                                verifyRetryCount++
                                delay(flightVerifyData.meta.refreshTime * 1000.toLong())
                                verifyCartData(query, totalPrice, contactName, contactEmail, contactPhone, contactCountry, checkVoucherQuery, addToCartQuery, idempotencyKey, getCartQuery)
                            } else {
                                verifyRetryCount = 0
                                flightVerifyResult.value = Fail(MessageErrorException(FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY))
                            }
                        }
                    } else {
                        val flightVerifyData = data.await()
                        if (!flightVerifyData.meta.needRefresh && flightVerifyData.data.cartItems.isNotEmpty()) {
                            verifyRetryCount = 0
                            flightVerifyResult.value = Success(flightVerifyData)
                            pastVerifyParam = generateVerifyParam(bookingVerifyParam)
                        } else {
                            if (flightVerifyData.meta.needRefresh && flightVerifyData.meta.maxRetry >= verifyRetryCount) {
                                verifyRetryCount++
                                delay(flightVerifyData.meta.refreshTime * 1000.toLong())
                                verifyCartData(query, totalPrice, contactName, contactEmail, contactPhone, contactCountry, checkVoucherQuery, addToCartQuery, idempotencyKey, getCartQuery)
                            } else {
                                verifyRetryCount = 0
                                flightVerifyResult.value = Fail(MessageErrorException(FlightErrorConstant.FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY))
                            }
                        }
                    }

                }) {
                    flightVerifyResult.value = Fail(it)
                }
            } else {
                //atc -> getcart -> verify
                refreshCartId(addToCartQuery, getCartQuery, checkVoucherQuery, idempotencyKey, bookingVerifyParam, query)
            }
        }
    }

    private fun refreshCartId(query: String, getCartQuery: String, checkVoucherQuery: String, idempotencyKey: String, bookingVerifyParam: FlightVerifyParam,
                              verifyQuery: String) {
        val addToCartParam = createAddToCartParam(idempotencyKey)
        val param = mapOf(PARAM_ATC to addToCartParam)
        //if add to cart success -> proceed to getCart with the id.
        launchCatchError(block = {
            val addToCartData = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(query, FlightAddToCartData.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightAddToCartData.Response>()

            flightBookingParam.cartId = addToCartData.addToCartData.id
            bookingVerifyParam.cartItems[0].metaData.cartId = addToCartData.addToCartData.id
            getCart(getCartQuery, getCartId(), true, bookingVerifyParam, verifyQuery, checkVoucherQuery)
        })
        {
            flightCartResult.value = Fail(it)
        }
    }

    private fun generateVerifyParam(bookingVerifyParam: FlightVerifyParam): String {
        val gson = Gson()
        return gson.toJson(bookingVerifyParam)
        Log.d("pastverifyparam", pastVerifyParam)
    }

    fun validateFields(contactName: String, contactEmail: String, contactPhone: String): Boolean {
        var isValid = true

        if (contactName.isEmpty()) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_name_empty_error
        } else if (contactName.isNotEmpty() && !isAlphabetAndSpaceOnly(contactName)) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_name_alpha_space_error
        } else if (contactEmail.isEmpty()) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_email_empty_error
        } else if (!isValidEmail(contactEmail)) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_email_invalid_error
        } else if (!isEmailWithoutProhibitSymbol(contactEmail)) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_email_invalid_error
        } else if (contactPhone.isEmpty()) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_phone_empty_error
        } else if (contactPhone.isNotEmpty() && !isNumericOnly(contactPhone)) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_phone_invalid_error
        } else if (contactPhone.length > 13) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_phone_max_length_error
        } else if (contactPhone.length < 9) {
            isValid = false
            errorToastMessageData.value = R.string.flight_booking_contact_phone_min_length_error
        } else {
            val passengerViewModels = flightPassengersData.value ?: listOf()
            if (!isAllPassengerFilled(passengerViewModels)) {
                isValid = false
                errorToastMessageData.value = R.string.flight_booking_passenger_not_fullfilled_error
            }
        }
        if (isValid) errorToastMessageData.value = 0
        return isValid
    }

    private fun isAllPassengerFilled(passengerViewModels: List<FlightBookingPassengerViewModel>): Boolean {
        var isvalid = true
        for (flightBookingPassengerViewModel in passengerViewModels) {
            if (flightBookingPassengerViewModel.passengerFirstName == null) {
                isvalid = false
                break
            } else if (flightBookingPassengerViewModel.passengerLastName == null) {
                isvalid = false
                break
            }
        }
        return isvalid
    }

    private fun isNumericOnly(expression: String): Boolean {
        val pattern = Pattern.compile("^[0-9\\s]*$")
        val matcher = pattern.matcher(expression)
        return matcher.matches()
    }

    private fun isAlphabetAndSpaceOnly(string: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z\\s]*$")
        val matcher = pattern.matcher(string)
        return matcher.matches()
    }

    private fun isEmailWithoutProhibitSymbol(contactEmail: String): Boolean =
            !contactEmail.contains("+")

    private fun isValidEmail(contactEmail: String): Boolean =
            Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() &&
                    !contactEmail.contains(".@") && !contactEmail.contains("@.")

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
                if (!passenger.passengerBirthdate.isNullOrEmpty()) flightVerifyPassenger.dob = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, passenger.passengerBirthdate))
                if (!flightIsDomestic()) {
                    flightVerifyPassenger.nationality = passenger.passportNationality?.countryId
                            ?: ""
                    flightVerifyPassenger.passportNumber = passenger.passportNumber ?: ""
                    flightVerifyPassenger.passportCountry = passenger.passportIssuerCountry?.countryId
                            ?: ""
                    flightVerifyPassenger.passportExpire = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, passenger.passportExpiredDate))
                            ?: ""
                }

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
                        amenity.type = Amenity.LUGGAGE
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

    fun onTravellerAsPassenger(isEnable: Boolean, userName: String) {
        val userProfile = if (profileResult.value is Success) (profileResult.value as Success).data else ProfileInfo()
        val passengers = (flightPassengersData.value ?: listOf()).toMutableList()
        if (passengers.isNotEmpty() && userName.isNotEmpty()) {
            val passenger = FlightBookingPassengerViewModel()
            passenger.passengerLocalId = 1
            passenger.type = FlightBookingPassenger.ADULT
            passenger.flightBookingLuggageMetaViewModels = arrayListOf()
            passenger.flightBookingMealMetaViewModels = arrayListOf()
            if (isEnable) {
                passenger.headerTitle = userName
                passenger.passengerFirstName = userName
                if (getMandatoryDOB()) passenger.passengerBirthdate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, userProfile.birthday))
            } else {
                if (passengers[0].passengerFirstName.equals(userName, true)) passenger.headerTitle = String.format("Penumpang dewasa")
                else passenger.passengerFirstName = passengers[0].passengerFirstName
            }
            passengers[0] = passenger
            flightPassengersData.value = passengers
        }
    }

    fun addPassengerAmenitiesPrices() {
        val flightBookingPassengers = flightPassengersData.value ?: listOf()
        // amenities
        val meals = hashMapOf<String, Int>()
        val mealsCount = hashMapOf<String, Int>()
        val luggages = hashMapOf<String, Int>()
        val luggageCount = hashMapOf<String, Int>()

        for (flightPassengerViewModel in flightBookingPassengers) {
            for (flightBookingAmenityMetaViewModel in flightPassengerViewModel.flightBookingMealMetaViewModels) {
                for (flightBookingAmenityViewModel in flightBookingAmenityMetaViewModel.amenities) {
                    if (meals[flightBookingAmenityMetaViewModel.description] != null) {
                        var total = meals[flightBookingAmenityMetaViewModel.description]!!
                        total += flightBookingAmenityViewModel.priceNumeric
                        meals[flightBookingAmenityMetaViewModel.description] = total

                        var count = mealsCount[flightBookingAmenityMetaViewModel.description] ?: 1
                        mealsCount[flightBookingAmenityMetaViewModel.description] = count + 1
                    } else {
                        meals[flightBookingAmenityMetaViewModel.description] = flightBookingAmenityViewModel.priceNumeric
                        mealsCount[flightBookingAmenityMetaViewModel.description] = 1
                    }
                }
            }
            for (flightBookingLuggageMetaViewModel in flightPassengerViewModel.flightBookingLuggageMetaViewModels) {
                for (flightBookingLuggageViewModel in flightBookingLuggageMetaViewModel.amenities) {
                    if (luggages[flightBookingLuggageMetaViewModel.description] != null) {
                        var total = luggages[flightBookingLuggageMetaViewModel.description]!!
                        total += flightBookingLuggageViewModel.priceNumeric
                        luggages[flightBookingLuggageMetaViewModel.description] = total

                        var count = luggageCount[flightBookingLuggageMetaViewModel.description] ?: 1
                        luggageCount[flightBookingLuggageMetaViewModel.description] = count + 1
                    } else {
                        luggages[flightBookingLuggageMetaViewModel.description] = flightBookingLuggageViewModel.priceNumeric
                        luggageCount[flightBookingLuggageMetaViewModel.description] = 1
                    }
                }
            }
        }

        val prices = listOf<FlightCart.PriceDetail>().toMutableList()
        for ((key, value) in meals) {
            val count = mealsCount[key] ?: 1
            if (count > 1) prices.add(FlightCart.PriceDetail(String.format("%s %s (x%s)", "Makanan", key, count), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
            else prices.add(FlightCart.PriceDetail(String.format("%s %s", "Makanan",
                    key), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
        }
        for ((key, value) in luggages) {
            val count = luggageCount[key] ?: 1
            if (count > 1) prices.add(FlightCart.PriceDetail(String.format("%s %s (x%s)", "Bagasi", key, count), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
            else prices.add(FlightCart.PriceDetail(String.format("%s %s", "Bagasi", key),
                    FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
        }
        flightAmenityPriceData.value = prices
    }

    fun updateOtherPriceData(priceDetail: List<FlightCart.PriceDetail>) {
        flightOtherPriceData.value = priceDetail
    }

    fun updateFlightPriceData(priceDetail: List<FlightCart.PriceDetail>) {
        flightPriceData.value = priceDetail
    }

    fun updateFlightDetailPriceData(newPrices: List<FlightCart.NewPrice>) {
        for (newPrice in newPrices) {
            for (item in flightDetailViewModels) {
                if (newPrice.id.equals(item.id)) {
                    item.adultNumericPrice = newPrice.fare.adultNumeric
                    item.infantNumericPrice = newPrice.fare.infantNumeric
                    item.childNumericPrice = newPrice.fare.childNumeric
                    item.totalNumeric = item.adultNumericPrice + item.infantNumericPrice + item.childNumericPrice
                }
            }
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

    fun getDepartureJourney(): FlightDetailViewModel? = getRouteForFlightDetail(getDepartureId())
    fun getReturnJourney(): FlightDetailViewModel? = getRouteForFlightDetail(getReturnId())
    fun getInvoiceId(): String {
        return if (flightVerifyResult.value is Success) (flightVerifyResult.value as Success<FlightVerify.FlightVerifyMetaAndData>).data.data.cartItems[0].metaData.invoiceId
        else ""
    }

    fun getUserId(): String {
        return if (profileResult.value is Success<ProfileInfo>) (profileResult.value as Success<ProfileInfo>).data.userId
        else ""
    }

    fun getRouteForFlightDetail(id: String): FlightDetailViewModel? {
        for (item in flightDetailViewModels) {
            if (item.id.equals(id, false)) {
                return item
            }
        }
        return null
    }

    fun onCancelAppliedVoucher(rawQuery: String) {
        launchCatchError(block = {
            withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCancelVoucher.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCancelVoucher>()
        }) { }
    }

    fun getLuggageViewModels(): List<FlightBookingAmenityMetaViewModel> {
        return if (flightCartResult.value is Success) (flightCartResult.value as Success<FlightCartViewEntity>).data.luggageModels
        else arrayListOf()
    }

    fun getMealViewModels(): List<FlightBookingAmenityMetaViewModel> {
        return if (flightCartResult.value is Success) (flightCartResult.value as Success<FlightCartViewEntity>).data.mealModels
        else arrayListOf()
    }

    fun getMandatoryDOB(): Boolean {
        return flightBookingParam.isMandatoryDob
    }

    fun onInsuranceChanges(insurance: FlightCart.Insurance, checked: Boolean) {
        val otherPrices = flightOtherPriceData.value?.toMutableList() ?: mutableListOf()
        if (checked) {
            val index = flightBookingParam.insurances.indexOf(insurance)
            if (index == -1) {
                flightBookingParam.insurances.add(insurance)
                otherPrices.add(FlightCart.PriceDetail(label = String.format("%s (x%d)", insurance.name, flightPassengersData.value?.size
                        ?: 1),
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

    private suspend fun checkVoucher(query: String, cartId: String): FlightVerify.PromoEligibility {
        val promoEligibility = FlightVerify.PromoEligibility()
        val promoCode = (flightPromoResult.value as FlightPromoViewEntity).promoData.promoCode
        val params = mapOf(PARAM_CART_ID to cartId, PARAM_VOUCHER_CODE to promoCode)
        try {
            val voucher = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(query, FlightVoucher.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightVoucher.Response>().flightVoucher

            promoEligibility.message = voucher.message
            promoEligibility.success = true
            return promoEligibility
        } catch (e: Exception) {
            return promoEligibility
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

    fun setSearchParam(depatureId: String, arrivalId: String,
                       departureTerm: String, arrivalTerm: String,
                       searchParam: FlightSearchPassDataViewModel,
                       flightPriceViewModel: FlightPriceViewModel) {
        flightBookingParam.departureId = depatureId
        flightBookingParam.returnId = arrivalId
        flightBookingParam.departureTerm = departureTerm
        flightBookingParam.returnTerm = arrivalTerm
        flightBookingParam.searchParam = searchParam
        flightBookingParam.flightPriceViewModel = flightPriceViewModel
    }

    fun addToCart(query: String, getCartQuery: String = "", idempotencyKey: String) {

        val addToCartParam = createAddToCartParam(idempotencyKey)
        val param = mapOf(PARAM_ATC to addToCartParam)
        //if add to cart success -> proceed to getCart with the id.
        launchCatchError(block = {
            val addToCartData = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(query, FlightAddToCartData.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightAddToCartData.Response>()

            flightBookingParam.cartId = addToCartData.addToCartData.id
            if (getCartQuery.isNotEmpty()) getCart(getCartQuery, getCartId())
        })
        {
            flightCartResult.value = Fail(it)
        }
    }

    private fun createAddToCartParam(idempotencyKey: String): FlightAddToCartParam {
        val addToCartParam = FlightAddToCartParam()

        addToCartParam.flight.adult = flightBookingParam.searchParam.flightPassengerViewModel.adult
        addToCartParam.flight.child = flightBookingParam.searchParam.flightPassengerViewModel.children
        addToCartParam.flight.infant = flightBookingParam.searchParam.flightPassengerViewModel.infant
        addToCartParam.flight.flightClass = flightBookingParam.searchParam.flightClass.id
        addToCartParam.idempotencyKey = idempotencyKey
        addToCartParam.did = 4
        addToCartParam.ipAddress = FlightRequestUtil.getLocalIpAddress()
        addToCartParam.userAgent = FlightRequestUtil.getUserAgentForApiCall()
        addToCartParam.flight.combo = flightBookingParam.flightPriceViewModel.comboKey ?: ""
        addToCartParam.flight.destination.add(FlightAddToCartParam.FlightDestination(flightBookingParam.departureId, flightBookingParam.departureTerm))
        if (flightBookingParam.returnId.isNotEmpty()) addToCartParam.flight.destination.add(FlightAddToCartParam.FlightDestination(flightBookingParam.returnId, flightBookingParam.returnTerm))

        return addToCartParam
    }

    fun getDepartureId(): String = flightBookingParam.departureId
    fun getReturnId(): String = flightBookingParam.returnId
    fun getDepartureDate(): String = flightBookingParam.departureDate

    fun checkOutCart(query: String, price: Int) {

        val checkoutParam = createCheckoutParam(getCartId(), price)
        val params = mapOf(PARAM_VERIFY_CART to checkoutParam)

        launchCatchError(block = {
            val checkOutData = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(query, FlightCheckoutData.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCheckoutData.Response>().flightCheckout

            flightCheckoutResult.value = Success(checkOutData)
        })
        {
            flightCartResult.value = Fail(it)
        }
    }

    private fun createCheckoutParam(cartId: String, price: Int): FlightCheckoutParam {
        val checkoutParam = FlightCheckoutParam()
        val cartItem = FlightCheckoutParam.CartItem()
        cartItem.productId = 27
        cartItem.quantity = 1
        cartItem.metaData = FlightCheckoutParam.MetaData(cartId,
                (flightVerifyResult.value as Success<FlightVerify.FlightVerifyMetaAndData>).data.data.cartItems[0].metaData.invoiceId,
                FlightRequestUtil.getLocalIpAddress(),
                FlightRequestUtil.getUserAgentForApiCall(),
                4)
        cartItem.configuration.price = price

        checkoutParam.cartItems.add(cartItem)
        checkoutParam.promoCode = (flightPromoResult.value as FlightPromoViewEntity).promoData.promoCode

        return checkoutParam
    }

    fun proceedCheckoutWithoutLuggage(query: String, price: Int) {
        val passengerViewModels = flightPassengersData.value ?: listOf()
        for (passenger in passengerViewModels) {
            passenger.flightBookingLuggageMetaViewModels = listOf()
        }
        flightPassengersData.value = passengerViewModels
        checkOutCart(query, price)
    }

    companion object {
        val PARAM_CART_ID = "cartID"
        val PARAM_VERIFY_CART = "data"
        val PARAM_ATC = "param"
        val PARAM_VOUCHER_CODE = "voucherCode"
    }

}