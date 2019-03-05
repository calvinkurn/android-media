package com.tokopedia.flight.bookingV2.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.data.model.request.DataRequest
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest
import com.tokopedia.flight.bookingV2.data.entity.AddToCartEntity
import com.tokopedia.flight.bookingV2.data.entity.GetCartEntity
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi
import com.tokopedia.user.session.UserSession
import rx.Observable
import javax.inject.Inject

/**
 * @author by furqan on 04/03/19
 */
class FlightBookingCartDataSource @Inject constructor(private val flightApi: FlightApi,
                                                      private val userSession: UserSession,
                                                      flightCartJsonDeserializer: FlightCartJsonDeserializer) {

    private val gsonWithDeserializer: Gson = GsonBuilder().registerTypeAdapter(GetCartEntity::class.java,
            flightCartJsonDeserializer).create()

    fun addToCart(request: FlightCartRequest, idEmpotencyKey: String): Observable<AddToCartEntity> =
            flightApi.addToCartV11(DataRequest(request), idEmpotencyKey, userSession.userId)
                    .flatMap {
                        Observable.just(it.data)
                    }

    fun getCart(cartId: String): Observable<GetCartEntity> =
            flightApi.getCart(cartId)
                    .map {
                        gsonWithDeserializer.fromJson(it.body(), GetCartEntity::class.java)
                    }
}