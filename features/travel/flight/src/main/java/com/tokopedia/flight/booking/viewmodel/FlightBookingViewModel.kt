package com.tokopedia.flight.booking.viewmodel

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.data.*
import com.tokopedia.flight.booking.data.mapper.FlightBookingMapper
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * @author by jessica on 2019-10-25
 */

class FlightBookingViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 private val travelTickerUseCase: TravelTickerCoroutineUseCase,
                                                 private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    private val _flightCartResult = MutableLiveData<Result<FlightCartViewEntity>>() //journey, insurance option, luggage option and meal option
    val flightCartResult: LiveData<Result<FlightCartViewEntity>>
        get() = _flightCartResult

    private val _flightVerifyResult = MutableLiveData<Result<FlightVerify.FlightVerifyMetaAndData>>() //flightVerify
    val flightVerifyResult: LiveData<Result<FlightVerify.FlightVerifyMetaAndData>>
        get() = _flightVerifyResult

    private val _flightCheckoutResult = MutableLiveData<Result<FlightCheckoutData>>() //flightCheckout
    val flightCheckoutResult: LiveData<Result<FlightCheckoutData>>
        get() = _flightCheckoutResult

    private val _flightPromoResult = MutableLiveData<FlightPromoViewEntity>() //promoData
    val flightPromoResult: LiveData<FlightPromoViewEntity>
        get() = _flightPromoResult

    private val _profileResult = MutableLiveData<Result<ProfileInfo>>() //profileData from userSession
    val profileResult: LiveData<Result<ProfileInfo>>
        get() = _profileResult

    private val _flightPassengersData = MutableLiveData<List<FlightBookingPassengerModel>>() //passengerData
    val flightPassengersData: LiveData<List<FlightBookingPassengerModel>>
        get() = _flightPassengersData

    private val _errorToastMessageData = MutableLiveData<Int>()
    val errorToastMessageData: LiveData<Int>
        get() = _errorToastMessageData

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    //priceListData
    private val _flightPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightPriceData: LiveData<List<FlightCart.PriceDetail>>
        get() = _flightPriceData

    private val _flightOtherPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightOtherPriceData: LiveData<List<FlightCart.PriceDetail>>
        get() = _flightOtherPriceData

    private val _flightAmenityPriceData = MutableLiveData<List<FlightCart.PriceDetail>>()
    val flightAmenityPriceData: LiveData<List<FlightCart.PriceDetail>>
        get() = _flightAmenityPriceData

    //route for flightDetail
    var flightDetailModels: List<FlightDetailModel> = listOf()

    private var flightBookingParam = FlightBookingModel()

    var retryCount = 0
    var verifyRetryCount = 0
    var pastVerifyParam = ""
    var isStillLoading = false

    init {
        _flightPriceData.value = listOf()
        _flightOtherPriceData.value = listOf()
        _flightAmenityPriceData.value = listOf()
        _flightPromoResult.value = FlightPromoViewEntity()
        _flightPassengersData.value = listOf()
    }

    fun fetchTickerData() {
        launch(dispatcherProvider.io) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.FLIGHT, TravelTickerFlightPage.BOOK)
            mutableTickerData.postValue(tickerData)
        }
    }

    fun getCart(rawQuery: String, cartId: String, autoVerify: Boolean = false, bookingVerifyParam: FlightVerifyParam? = null,
                verifyQuery: String = "", checkVoucherQuery: String = "", isRefreshCart: Boolean = false) {
        val params = mapOf(PARAM_CART_ID to cartId)
        launchCatchError(block = {
            val data = withContext(dispatcherProvider.main) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCart.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCart.Response>().flightCart

            if (data.cartData.id.isNotBlank() && !data.meta.needRefresh) {
                if (autoVerify && bookingVerifyParam != null && verifyQuery.isNotEmpty() && checkVoucherQuery.isNotEmpty()) {
                    verifyCartData(verifyQuery, bookingVerifyParam, checkVoucherQuery)
                    isStillLoading = true
                } else {
                    flightBookingParam.departureDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, data.cartData.flight.journeys[0].departureTime))
                    flightBookingParam.isDomestic = data.cartData.flight.isDomestic
                    flightBookingParam.isMandatoryDob = data.cartData.flight.mandatoryDob
                    flightDetailModels = FlightBookingMapper.mapToFlightDetail(data.cartData.flight, data.included, flightBookingParam.flightPriceModel)
                    if (flightPassengersData.value?.isEmpty() != false && !isRefreshCart) {
                        _flightPromoResult.postValue(FlightBookingMapper.mapToFlightPromoViewEntity(data.cartData.voucher))
                        _flightPassengersData.postValue(FlightBookingMapper.mapToFlightPassengerEntity(data.cartData.flight.adult,
                                data.cartData.flight.child, data.cartData.flight.infant))
                    }
                    _flightPriceData.postValue(data.cartData.flight.priceDetail)
                    _flightCartResult.postValue(Success(FlightBookingMapper.mapToFlightCartView(data, isRefreshCart)))
                }
                retryCount = 0
            } else {
                if (data.meta.needRefresh && data.meta.maxRetry >= retryCount) {
                    retryCount++
                    delay(data.meta.refreshTime * 1000.toLong())
                    getCart(rawQuery, cartId, autoVerify, bookingVerifyParam, verifyQuery, checkVoucherQuery)
                } else {
                    retryCount = 0
                    _flightCartResult.postValue(Fail(MessageErrorException(FlightErrorConstant.FLIGHT_ERROR_GET_CART_EXCEED_MAX_RETRY)))
                }
            }
        }) {
            _flightCartResult.postValue(Fail(it))
        }
    }

    fun validateDataAndVerifyCart(query: String, totalPrice: Int, contactName: String,
                                  contactEmail: String, contactPhone: String, contactCountry: String,
                                  checkVoucherQuery: String, addToCartQuery: String, idempotencyKey: String,
                                  getCartQuery: String) {

        if (validateFields(contactName, contactEmail, contactPhone)) {
            val bookingVerifyParam = createVerifyParam(totalPrice, getCartId(), contactName, contactEmail, contactPhone, contactCountry)

            if (pastVerifyParam.isEmpty() || convertVerifyParamToString(bookingVerifyParam) == pastVerifyParam) {
                verifyCartData(query, bookingVerifyParam, checkVoucherQuery)
            } else {
                refreshCartId(addToCartQuery, getCartQuery, checkVoucherQuery, idempotencyKey, bookingVerifyParam, query)
            }
        }
    }


    private fun verifyCartData(query: String, bookingVerifyParam: FlightVerifyParam, checkVoucherQuery: String) {
        val promoCode = (flightPromoResult.value as FlightPromoViewEntity).promoData.promoCode
        val params = mapOf(PARAM_VERIFY_CART to bookingVerifyParam)

        launchCatchError(context = dispatcherProvider.main, block = {
            val graphqlRequest = GraphqlRequest(query, FlightVerify.Response::class.java, params)
            val flightVerifyData = graphqlRepository.getReseponse(listOf(graphqlRequest))
                    .getSuccessData<FlightVerify.Response>().flightVerify

            if (!flightVerifyData.meta.needRefresh && flightVerifyData.data.cartItems.isNotEmpty()) {

                if (promoCode.isNotEmpty()) {
                    val checkPromoData = checkVoucher(checkVoucherQuery, getCartId())
                    flightVerifyData.data.cartItems[0].promoEligibility = checkPromoData
                }

                verifyRetryCount = 0
                isStillLoading = false
                _flightVerifyResult.postValue(Success(flightVerifyData))
                if (!flightVerifyData.data.cartItems[0].promoEligibility.success) {
                    //update UI promoData (reset)
                    _flightPromoResult.postValue(FlightPromoViewEntity(isCouponEnable = true))
                }
                pastVerifyParam = convertVerifyParamToString(bookingVerifyParam)
            } else {
                if (flightVerifyData.meta.needRefresh && flightVerifyData.meta.maxRetry >= verifyRetryCount) {
                    verifyRetryCount++
                    delay(flightVerifyData.meta.refreshTime * 1000.toLong())
                    verifyCartData(query, bookingVerifyParam, checkVoucherQuery)
                } else {
                    verifyRetryCount = 0
                    _flightVerifyResult.postValue(Fail(MessageErrorException(FlightErrorConstant.FLIGHT_ERROR_VERIFY_EXCEED_MAX_RETRY)))
                }
            }
        }) {
            isStillLoading = false
            _flightVerifyResult.postValue(Fail(it))
        }
    }

    private fun refreshCartId(query: String, getCartQuery: String, checkVoucherQuery: String, idempotencyKey: String, bookingVerifyParam: FlightVerifyParam,
                              verifyQuery: String) {
        val addToCartParam = createAddToCartParam(idempotencyKey)
        val param = mapOf(PARAM_ATC to addToCartParam)
        //if add to cart success -> proceed to getCart with the id.
        launchCatchError(context = dispatcherProvider.main, block = {
            val addToCartData = withContext(dispatcherProvider.main) {
                val graphqlRequest = GraphqlRequest(query, FlightAddToCartData.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightAddToCartData.Response>()

            flightBookingParam.cartId = addToCartData.addToCartData.id
            bookingVerifyParam.cartItems[0].metaData.cartId = addToCartData.addToCartData.id
            pastVerifyParam = convertVerifyParamToString(bookingVerifyParam)
            getCart(getCartQuery, getCartId(), true, bookingVerifyParam, verifyQuery, checkVoucherQuery)
        })
        {
            _flightCartResult.postValue(Fail(it))
        }
    }

    fun getProfile(rawQuery: String) {
        launchCatchError(block = {
            val profileInfo = withContext(dispatcherProvider.main) {
                val graphqlRequest = GraphqlRequest(rawQuery, ProfilePojo::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<ProfilePojo>().profileInfo
            profileInfo.phone = transformPhoneNum(profileInfo.phone)

            _profileResult.postValue(Success(profileInfo))
        })
        {
            _profileResult.postValue(Fail(it))
        }
    }

    fun onCancelAppliedVoucher(rawQuery: String) {
        launchCatchError(block = {
            withContext(dispatcherProvider.main) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightCancelVoucher.Response::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCancelVoucher>()
        }) { }
    }

    private fun convertVerifyParamToString(bookingVerifyParam: FlightVerifyParam): String {
        val gson = Gson()
        return gson.toJson(bookingVerifyParam.cartItems[0].metaData)
    }

    private fun validateFields(contactName: String, contactEmail: String, contactPhone: String): Boolean {
        var isValid = true

        if (contactName.isEmpty()) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_name_empty_error
        } else if (contactName.isNotEmpty() && !isAlphabetAndSpaceOnly(contactName)) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_name_alpha_space_error
        } else if (contactEmail.isEmpty()) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_email_empty_error
        } else if (!isValidEmail(contactEmail)) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_email_invalid_error
        } else if (!isEmailWithoutProhibitSymbol(contactEmail)) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_email_invalid_error
        } else if (contactPhone.isEmpty()) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_phone_empty_error
        } else if (contactPhone.isNotEmpty() && !isNumericOnly(contactPhone)) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_phone_invalid_error
        } else if (contactPhone.length > 13) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_phone_max_length_error
        } else if (contactPhone.length < 9) {
            isValid = false
            _errorToastMessageData.value = R.string.flight_booking_contact_phone_min_length_error
        } else {
            val passengerViewModels = flightPassengersData.value ?: listOf()
            if (!isAllPassengerFilled(passengerViewModels)) {
                isValid = false
                _errorToastMessageData.value = R.string.flight_booking_passenger_not_fullfilled_error
            }
        }
        if (isValid) _errorToastMessageData.value = 0
        return isValid
    }

    private fun isAllPassengerFilled(passengerModels: List<FlightBookingPassengerModel>): Boolean {
        var isvalid = true
        for (flightBookingPassengerViewModel in passengerModels) {
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
            PatternsCompat.EMAIL_ADDRESS.matcher(contactEmail).matches() &&
                    !contactEmail.contains(".@") && !contactEmail.contains("@.")

    private fun createVerifyParam(totalPrice: Int, cartId: String, contactName: String,
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
            cartItem.metaData.country = if (contactCountry.isNotEmpty()) contactCountry else "ID"
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
                    flightVerifyPassenger.passportExpire = passenger.passportExpiredDate?.let {
                        TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it))
                    } ?: ""
                }

                for (mealMeta in passenger.flightBookingMealMetaViewModels) {
                    for (meal in mealMeta.amenities) {
                        val amenity = FlightVerifyParam.Amenity()
                        amenity.journeyId = mealMeta.journeyId
                        amenity.departureAirportId = mealMeta.departureId
                        amenity.arrivalAirportId = mealMeta.arrivalId
                        amenity.type = FlightBookingMapper.AMENITY_MEAL
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
                        amenity.type = FlightBookingMapper.AMENITY_LUGGAGE
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
            e.printStackTrace()
        }

        return flightVerifyParam
    }

    fun setPassengerModels(passengerModels: List<FlightBookingPassengerModel>) {
        _flightPassengersData.value = passengerModels
    }

    fun setPriceData(priceData: List<FlightCart.PriceDetail>) {
        _flightPriceData.value = priceData
    }

    fun setOtherPriceData(priceData: List<FlightCart.PriceDetail>) {
        _flightOtherPriceData.value = priceData
    }

    fun setAmenityPriceData(priceData: List<FlightCart.PriceDetail>) {
        _flightAmenityPriceData.value = priceData
    }

    fun onPassengerResultReceived(passengerModel: FlightBookingPassengerModel) {
        val passengerViewModels = flightPassengersData.value!!.toMutableList()
        val indexPassenger = passengerViewModels?.indexOf(passengerModel) ?: -1
        if (indexPassenger != -1) {
            passengerViewModels[indexPassenger] = passengerModel
            _flightPassengersData.value = passengerViewModels
        }
        addPassengerAmenitiesPrices()
    }

    fun onTravellerAsPassenger(userName: String): FlightBookingPassengerModel {
        val userProfile = if (profileResult.value is Success) (profileResult.value as Success).data else ProfileInfo()
        val passenger = FlightBookingPassengerModel()
        passenger.passengerLocalId = 1
        passenger.type = FlightBookingPassenger.ADULT
        passenger.flightBookingLuggageMetaViewModels = arrayListOf()
        passenger.flightBookingMealMetaViewModels = arrayListOf()
        passenger.headerTitle = userName
        passenger.passengerFirstName = userName
        if (getMandatoryDOB()) passenger.passengerBirthdate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, userProfile.birthday))
        return passenger
    }

    fun resetFirstPassenger() {
        val passengers = (flightPassengersData.value!!).toMutableList()
        if (passengers.isNotEmpty()) {
            val passenger = FlightBookingPassengerModel()
            passenger.passengerLocalId = 1
            passenger.type = FlightBookingPassenger.ADULT
            passenger.flightBookingLuggageMetaViewModels = arrayListOf()
            passenger.flightBookingMealMetaViewModels = arrayListOf()
            passenger.headerTitle = String.format("Penumpang dewasa")
            passengers[0] = passenger
            _flightPassengersData.value = passengers
        }
    }

    private fun addPassengerAmenitiesPrices(): Int {
        val flightBookingPassengers = flightPassengersData.value!!
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
        var grandTotalAmenityPrice = 0
        for ((key, value) in meals) {
            val count = mealsCount[key] ?: 1
            if (count > 1) prices.add(FlightCart.PriceDetail(String.format("%s %s (x%s)", "Makanan", key, count), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
            else prices.add(FlightCart.PriceDetail(String.format("%s %s", "Makanan",
                    key), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
            grandTotalAmenityPrice += value
        }
        for ((key, value) in luggages) {
            val count = luggageCount[key] ?: 1
            if (count > 1) prices.add(FlightCart.PriceDetail(String.format("%s %s (x%s)", "Bagasi", key, count), FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
            else prices.add(FlightCart.PriceDetail(String.format("%s %s", "Bagasi", key),
                    FlightCurrencyFormatUtil.convertToIdrPrice(value), value))
            grandTotalAmenityPrice += value
        }
        _flightAmenityPriceData.value = prices
        return grandTotalAmenityPrice
    }

    fun updatePromoData(promoData: PromoData) {
        flightPromoResult.value!!.let {
            it.promoData = promoData
            _flightPromoResult.value = it
        }
    }

    fun updateFlightPriceData(priceDetail: List<FlightCart.PriceDetail>) {
        _flightPriceData.value = priceDetail
    }

    fun updateFlightDetailPriceData(newPrices: List<FlightCart.NewPrice>) {
        for (newPrice in newPrices) {
            for (item in flightDetailModels) {
                if (newPrice.id == item.id) {
                    item.adultNumericPrice = newPrice.fare.adultNumeric
                    item.infantNumericPrice = newPrice.fare.infantNumeric
                    item.childNumericPrice = newPrice.fare.childNumeric
                    item.totalNumeric = item.adultNumericPrice + item.infantNumericPrice + item.childNumericPrice
                }
            }
        }
    }

    fun onInsuranceChanges(insurance: FlightCart.Insurance, checked: Boolean) {
        val otherPrices = flightOtherPriceData.value!!.toMutableList()
        val insuranceTemp = flightBookingParam.insurances.toMutableList()
        if (checked) {
            val index = flightBookingParam.insurances.indexOf(insurance)
            if (index == -1) {
                insuranceTemp.add(insurance)
                flightBookingParam.insurances = insuranceTemp
                otherPrices.add(FlightCart.PriceDetail(label = String.format("%s (x%d)", insurance.name, flightPassengersData.value!!.size),
                        priceNumeric = insurance.totalPriceNumeric, price = FlightCurrencyFormatUtil.convertToIdrPrice(insurance.totalPriceNumeric),
                        priceDetailId = insurance.id))
            }

        } else {
            val index = flightBookingParam.insurances.indexOf(insurance)
            if (index != -1) {
                insuranceTemp.removeAt(index)
                flightBookingParam.insurances = insuranceTemp
                for ((index, price) in otherPrices.withIndex()) {
                    if (price.priceDetailId == insurance.id) {
                        otherPrices.removeAt(index)
                        break
                    }
                }
            }
        }
        _flightOtherPriceData.value = otherPrices
    }

    private suspend fun checkVoucher(query: String, cartId: String): FlightVerify.PromoEligibility {
        val promoEligibility = FlightVerify.PromoEligibility()
        val flightPromoViewEntity = (flightPromoResult.value as FlightPromoViewEntity)
        val params = mapOf(PARAM_CART_ID to cartId, PARAM_VOUCHER_CODE to flightPromoViewEntity.promoData.promoCode)
        try {
            val voucher = withContext(dispatcherProvider.main) {
                val graphqlRequest = GraphqlRequest(query, FlightVoucher.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightVoucher.Response>().flightVoucher

            promoEligibility.message = voucher.message
            promoEligibility.success = true

            //update UI promoData
            flightPromoViewEntity.promoData.description = voucher.message
            _flightPromoResult.value = flightPromoViewEntity

            return promoEligibility
        } catch (e: Exception) {
            promoEligibility.success = false
            if (!e.message.isNullOrEmpty()) {
                val error = mapThrowableToFlightError(e.message!!)
                promoEligibility.message = error.title
                if (error.id == "4") promoEligibility.message += ". Promo akan dihapus jika lanjut bayar"
            }
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
                       searchParam: FlightSearchPassDataModel,
                       flightPriceModel: FlightPriceModel) {
        flightBookingParam.departureId = depatureId
        flightBookingParam.returnId = arrivalId
        flightBookingParam.departureTerm = departureTerm
        flightBookingParam.returnTerm = arrivalTerm
        flightBookingParam.searchParam = searchParam
        flightBookingParam.flightPriceModel = flightPriceModel
    }

    fun addToCart(query: String, getCartQuery: String = "", idempotencyKey: String) {

        val addToCartParam = createAddToCartParam(idempotencyKey)
        val param = mapOf(PARAM_ATC to addToCartParam)
        //if add to cart success -> proceed to getCart with the id.
        launchCatchError(block = {
            val addToCartData = withContext(dispatcherProvider.main) {
                val graphqlRequest = GraphqlRequest(query, FlightAddToCartData.Response::class.java, param)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightAddToCartData.Response>()

            flightBookingParam.cartId = addToCartData.addToCartData.id
            if (getCartQuery.isNotEmpty()) getCart(getCartQuery, getCartId())
        })
        {
            _flightCartResult.postValue(Fail(it))
        }
    }

    private fun createAddToCartParam(idempotencyKey: String): FlightAddToCartParam {
        val addToCartParam = FlightAddToCartParam()

        addToCartParam.flight.adult = flightBookingParam.searchParam.flightPassengerModel.adult
        addToCartParam.flight.child = flightBookingParam.searchParam.flightPassengerModel.children
        addToCartParam.flight.infant = flightBookingParam.searchParam.flightPassengerModel.infant
        addToCartParam.flight.flightClass = flightBookingParam.searchParam.flightClass.id
        addToCartParam.idempotencyKey = idempotencyKey
        addToCartParam.did = 4
        addToCartParam.requestId = flightBookingParam.searchParam.searchRequestId
        addToCartParam.ipAddress = FlightRequestUtil.getLocalIpAddress()
        addToCartParam.userAgent = FlightRequestUtil.getUserAgentForApiCall()
        addToCartParam.flight.combo = flightBookingParam.flightPriceModel.comboKey
        addToCartParam.flight.destination.add(FlightAddToCartParam.FlightDestination(flightBookingParam.departureId, flightBookingParam.departureTerm))
        if (flightBookingParam.returnId.isNotEmpty()) addToCartParam.flight.destination.add(FlightAddToCartParam.FlightDestination(flightBookingParam.returnId, flightBookingParam.returnTerm))

        return addToCartParam
    }

    fun checkOutCart(query: String, price: Int) {
        val checkoutParam = createCheckoutParam(getCartId(), price)
        val params = mapOf(PARAM_VERIFY_CART to checkoutParam)

        launchCatchError(block = {
            val checkOutData = withContext(dispatcherProvider.main) {
                val graphqlRequest = GraphqlRequest(query, FlightCheckoutData.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<FlightCheckoutData.Response>().flightCheckout

            _flightCheckoutResult.postValue(Success(checkOutData))
        })
        {
            _flightCheckoutResult.postValue(Fail(it))
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

    fun proceedCheckoutWithoutLuggage(checkVoucherQuery: String, verifyQuery: String,
                                      totalPriceWithoutAmenities: Int, contactName: String, contactEmail: String,
                                      contactPhone: String, contactCountry: String) {
        val passengerViewModels = flightPassengersData.value!!
        for (passenger in passengerViewModels) {
            passenger.flightBookingLuggageMetaViewModels = listOf()
        }
        _flightPassengersData.value = passengerViewModels

        val amenitiesPrice = addPassengerAmenitiesPrices()

        val bookingVerifyParam = createVerifyParam(
                totalPrice = totalPriceWithoutAmenities + amenitiesPrice,
                cartId = getCartId(),
                contactName = contactName,
                contactEmail = contactEmail,
                contactPhone = contactPhone,
                contactCountry = contactCountry)
        verifyCartData(verifyQuery, bookingVerifyParam, checkVoucherQuery)
    }

    private fun mapThrowableToFlightError(message: String): FlightError {
        val gson = Gson()
        val itemType = object : TypeToken<List<FlightError>>() {}.type
        return gson.fromJson<List<FlightError>>(message, itemType)[0]
    }

    fun getSearchParam(): FlightSearchPassDataModel = flightBookingParam.searchParam
    fun getFlightPriceModel(): FlightPriceModel = flightBookingParam.flightPriceModel
    fun getMandatoryDOB(): Boolean = flightBookingParam.isMandatoryDob
    fun getDepartureId(): String = flightBookingParam.departureId
    fun getReturnId(): String = flightBookingParam.returnId
    fun getDepartureTerm(): String = flightBookingParam.departureTerm
    fun getReturnTerm(): String = flightBookingParam.returnTerm
    fun getDepartureDate(): String = flightBookingParam.departureDate
    fun getDepartureJourney(): FlightDetailModel? = flightDetailModels.firstOrNull()
    fun getReturnJourney(): FlightDetailModel? = if (flightDetailModels.size > 1) flightDetailModels[1] else null
    fun flightIsDomestic(): Boolean = flightBookingParam.isDomestic
    fun getCartId(): String = flightBookingParam.cartId
    fun setCartId(cartId: String) {
        flightBookingParam.cartId = cartId
    }

    fun getFlightBookingParam(): FlightBookingModel = flightBookingParam
    fun setFlightBookingParam(flightBookingParam: FlightBookingModel) {
        this.flightBookingParam = flightBookingParam
    }

    fun getLuggageViewModels(): List<FlightBookingAmenityMetaModel> {
        return if (flightCartResult.value is Success) (flightCartResult.value as Success<FlightCartViewEntity>).data.luggageModels
        else arrayListOf()
    }

    fun getPassengerModels(): List<FlightBookingPassengerModel> {
        return flightPassengersData.value!!
    }

    fun getMealViewModels(): List<FlightBookingAmenityMetaModel> {
        return if (flightCartResult.value is Success) (flightCartResult.value as Success<FlightCartViewEntity>).data.mealModels
        else arrayListOf()
    }

    fun getInvoiceId(): String {
        return if (flightVerifyResult.value is Success) (flightVerifyResult.value as Success<FlightVerify.FlightVerifyMetaAndData>).data.data.cartItems[0].metaData.invoiceId
        else ""
    }

    fun getUserId(): String {
        return if (profileResult.value is Success<ProfileInfo>) (profileResult.value as Success<ProfileInfo>).data.userId
        else ""
    }

    fun getPriceData(): List<FlightCart.PriceDetail> {
        return flightPriceData.value!!
    }

    fun getOtherPriceData(): List<FlightCart.PriceDetail> {
        return flightOtherPriceData.value!!
    }

    fun getAmenityPriceData(): List<FlightCart.PriceDetail> {
        return flightAmenityPriceData.value!!
    }

    companion object {
        val PARAM_CART_ID = "cartID"
        val PARAM_VERIFY_CART = "data"
        val PARAM_ATC = "param"
        val PARAM_VOUCHER_CODE = "voucherCode"
    }

}