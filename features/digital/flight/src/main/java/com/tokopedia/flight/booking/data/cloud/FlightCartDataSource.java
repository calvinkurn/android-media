package com.tokopedia.flight.booking.data.cloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 11/13/17.
 */

public class FlightCartDataSource {
    private FlightApi flightApi;
    private UserSessionInterface userSession;
    private Gson gsonWithDeserializer;
    private FlightCartJsonDeserializer flightCartJsonDeserializer;

    @Inject
    public FlightCartDataSource(FlightApi flightApi, UserSessionInterface userSession, FlightCartJsonDeserializer flightCartJsonDeserializer) {
        this.flightApi = flightApi;
        this.userSession = userSession;
        this.flightCartJsonDeserializer = flightCartJsonDeserializer;
        this.gsonWithDeserializer = new GsonBuilder().registerTypeAdapter(CartEntity.class, this.flightCartJsonDeserializer).create();
    }

    public Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey) {
        return this.flightApi.addCart(new DataRequest<>(request), idEmpotencyKey, userSession.getUserId())
                .map(new Func1<Response<String>, CartEntity>() {
                    @Override
                    public CartEntity call(Response<String> stringResponse) {
                        return gsonWithDeserializer.fromJson(stringResponse.body(), CartEntity.class);
                    }
                });
    }
}
