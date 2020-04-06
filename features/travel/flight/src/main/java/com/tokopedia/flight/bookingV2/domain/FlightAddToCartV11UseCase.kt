package com.tokopedia.flight.bookingV2.domain

import android.text.TextUtils
import com.tokopedia.flight.bookingV2.data.cloud.requestbody.CartAirportRequest
import com.tokopedia.flight.bookingV2.data.cloud.requestbody.CartAttributesRequest
import com.tokopedia.flight.bookingV2.data.cloud.requestbody.FlightCartRequest
import com.tokopedia.flight.bookingV2.data.cloud.requestbody.FlightRequest
import com.tokopedia.flight.bookingV2.data.entity.AddToCartEntity
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.search.domain.FlightSearchJourneyByIdUseCase
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func2
import rx.functions.Func3
import javax.inject.Inject

/**
 * @author by furqan on 05/03/19
 */
class FlightAddToCartV11UseCase @Inject constructor(val flightRepository: FlightRepository,
                                                    private val flightSearchJourneyByIdUseCase: FlightSearchJourneyByIdUseCase)
    : UseCase<AddToCartEntity>() {

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartEntity> =
            createRequest(requestParams!!)
                    .flatMap {
                        flightRepository.addCartV11(it, it.idEmpotencyKey)
                    }


    private fun createRequest(requestParams: RequestParams): Observable<FlightCartRequest> {
        val request = FlightCartRequest()
        request.type = PARAM_CART_TYPE
        val attributesRequest = CartAttributesRequest()
        attributesRequest.did = DEFAULT_DEVICE_ID
        attributesRequest.ipAddress = FlightRequestUtil.getLocalIpAddress()
        attributesRequest.userAgent = FlightRequestUtil.getUserAgentForApiCall()
        attributesRequest.requestId = requestParams.getString(PARAM_REQUEST_ID, "")

        val flightRequest = FlightRequest()
        flightRequest.adult = requestParams.getInt(PARAM_ADULT, 0)
        flightRequest.child = requestParams.getInt(PARAM_CHILD, PARAM_DEFAULT_VALUE_INT)
        flightRequest.infant = requestParams.getInt(PARAM_INFANT, PARAM_DEFAULT_VALUE_INT)
        flightRequest.classFlight = requestParams.getInt(PARAM_CLASS, PARAM_DEFAULT_VALUE_INT)
        flightRequest.comboKey = requestParams.getString(PARAM_COMBO_KEY, PARAM_DEFAULT_VALUE)
        flightRequest.priceCurrency = DEFAULT_CURRENCY_ID
        flightRequest.price = requestParams.getInt(PARAM_PRICE, 0)

        val airportRequests = arrayListOf<CartAirportRequest>()
        flightRequest.destinations = airportRequests
        attributesRequest.flight = flightRequest
        request.cartAttributes = attributesRequest
        val departureId = requestParams.getString(PARAM_FLIGHT_DEPARTURE, PARAM_DEFAULT_VALUE)
        val arrivalId = requestParams.getString(PARAM_FLIGHT_RETURN, PARAM_DEFAULT_VALUE)

        request.idEmpotencyKey = requestParams.getString(PARAM_ID_EMPOTENCY_KEY, PARAM_DEFAULT_VALUE)

        return if (!TextUtils.isEmpty(arrivalId)) {
            Observable.zip<FlightJourneyModel, FlightJourneyModel, FlightCartRequest, FlightCartRequest>(
                    flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase.createRequestParams(departureId)),
                    flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase.createRequestParams(arrivalId)),
                    Observable.just<FlightCartRequest>(request),
                    object : Func3<FlightJourneyModel, FlightJourneyModel, FlightCartRequest, FlightCartRequest> {
                        override fun call(departureViewModel: FlightJourneyModel, arrivalViewModel: FlightJourneyModel, flightCartRequest: FlightCartRequest): FlightCartRequest {
                            val departureAirport = getCartAirportRequest(departureViewModel)
                            val arrivalAirport = getCartAirportRequest(arrivalViewModel)
                            val airportRequests = arrayListOf<CartAirportRequest>()
                            airportRequests.add(departureAirport)
                            airportRequests.add(arrivalAirport)
                            flightCartRequest.cartAttributes.flight.destinations = airportRequests
                            return flightCartRequest
                        }
                    })
        } else {
            Observable.zip<FlightJourneyModel, FlightCartRequest, FlightCartRequest>(
                    flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase.createRequestParams(departureId)),
                    Observable.just<FlightCartRequest>(request),
                    object : Func2<FlightJourneyModel, FlightCartRequest, FlightCartRequest> {
                        override fun call(departureViewModel: FlightJourneyModel, flightCartRequest: FlightCartRequest): FlightCartRequest {
                            val departureAirport = getCartAirportRequest(departureViewModel)
                            val airportRequests = ArrayList<CartAirportRequest>()
                            airportRequests.add(departureAirport)
                            flightCartRequest.cartAttributes.flight.destinations = airportRequests
                            return flightCartRequest
                        }
                    })
        }
    }

    private fun getCartAirportRequest(routeViewModel: FlightJourneyModel): CartAirportRequest {
        val departureAirport = CartAirportRequest()
        departureAirport.journeyId = routeViewModel.id
        departureAirport.term = routeViewModel.term
        return departureAirport
    }

    fun createRequestParam(adult: Int,
                           child: Int,
                           infant: Int,
                           flightClass: Int,
                           departureId: String,
                           idEmpotencyKey: String,
                           price: Int,
                           requestId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_ADULT, adult)
        requestParams.putInt(PARAM_CHILD, child)
        requestParams.putInt(PARAM_INFANT, infant)
        requestParams.putInt(PARAM_CLASS, flightClass)
        requestParams.putString(PARAM_FLIGHT_DEPARTURE, departureId)
        requestParams.putString(PARAM_ID_EMPOTENCY_KEY, idEmpotencyKey)
        requestParams.putInt(PARAM_PRICE, price)
        requestParams.putString(PARAM_REQUEST_ID, requestId)
        return requestParams
    }

    fun createRequestParam(adult: Int,
                           child: Int,
                           infant: Int,
                           flightClass: Int,
                           departureId: String,
                           returnId: String,
                           idEmpotencyKey: String,
                           price: Int,
                           comboKey: String,
                           requestId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_ADULT, adult)
        requestParams.putInt(PARAM_CHILD, child)
        requestParams.putInt(PARAM_INFANT, infant)
        requestParams.putInt(PARAM_CLASS, flightClass)
        requestParams.putString(PARAM_FLIGHT_DEPARTURE, departureId)
        requestParams.putString(PARAM_FLIGHT_RETURN, returnId)
        requestParams.putString(PARAM_ID_EMPOTENCY_KEY, idEmpotencyKey)
        requestParams.putInt(PARAM_PRICE, price)
        requestParams.putString(PARAM_COMBO_KEY, comboKey)
        requestParams.putString(PARAM_REQUEST_ID, requestId)
        return requestParams
    }

    companion object {
        const val PARAM_ADULT = "adult"
        const val PARAM_CHILD = "child"
        const val PARAM_INFANT = "infant"
        const val PARAM_CLASS = "class"
        const val PARAM_COMBO_KEY = "combo_key"
        const val PARAM_FLIGHT_DEPARTURE = "departure"
        const val PARAM_FLIGHT_RETURN = "return"
        const val PARAM_ID_EMPOTENCY_KEY = "id_empotency_key"
        private const val PARAM_REQUEST_ID = "request_id"

        private const val PARAM_DEFAULT_VALUE = ""
        private const val PARAM_DEFAULT_VALUE_INT = 0
        private const val PARAM_CART_TYPE = "add_cart"
        private const val PARAM_PRICE = "price"
        private const val PARAM_CURRENCY = "price_currency"
        private const val DEFAULT_CURRENCY_ID = 1
        private const val DEFAULT_DEVICE_ID = 5
    }
}