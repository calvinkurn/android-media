package com.tokopedia.flight.review.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.qualifier.FlightGsonPlainQualifier;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 12/7/17.
 */

public class FlightBookingDataSourceCloud {

    private final FlightApi flightApi;
    private UserSessionInterface userSession;
    private Gson gson;
    private FlightVerifyJsonDeserializer flightVerifyJsonDeserializer;

    @Inject
    public FlightBookingDataSourceCloud(FlightApi flightApi, UserSessionInterface userSession, @FlightGsonPlainQualifier Gson gson,
                                        FlightVerifyJsonDeserializer flightVerifyJsonDeserializer) {
        this.flightApi = flightApi;
        this.userSession = userSession;
        this.flightVerifyJsonDeserializer = flightVerifyJsonDeserializer;
        this.gson = new GsonBuilder().registerTypeAdapter(DataResponseVerify.class,
                flightVerifyJsonDeserializer).create();
    }

    public Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest) {
        return flightApi.verifyBooking(gson
                .fromJson(gson.toJson(verifyRequest), JsonElement.class).getAsJsonObject(), userSession.getUserId())
                .map(stringResponse -> gson.fromJson(stringResponse.body(), DataResponseVerify.class));
    }

    public Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request) {
        return flightApi.checkout(request, userSession.getUserId())
                .map(new Func1<Response<DataResponse<FlightCheckoutEntity>>, FlightCheckoutEntity>() {
                    @Override
                    public FlightCheckoutEntity call(Response<DataResponse<FlightCheckoutEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}
