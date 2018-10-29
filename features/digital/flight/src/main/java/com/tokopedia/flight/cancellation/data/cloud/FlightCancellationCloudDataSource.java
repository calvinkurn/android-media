package com.tokopedia.flight.cancellation.data.cloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.cancellation.data.cache.FlightCancellationReasonDataCacheSource;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancelPassengerEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationCloudDataSource {
    private FlightApi flightApi;
    private Gson gsonWithDeserializer;
    private FlightCancellationReasonDataCacheSource flightCancellationReasonDataCacheSource;
    private FlightCancellationJsonDeserializer flightCancellationJsonDeserializer;

    @Inject
    public FlightCancellationCloudDataSource(FlightApi flightApi,
                                             FlightCancellationReasonDataCacheSource flightCancellationReasonDataCacheSource,
                                             FlightCancellationJsonDeserializer flightCancellationJsonDeserializer) {
        this.flightApi = flightApi;
        this.flightCancellationReasonDataCacheSource = flightCancellationReasonDataCacheSource;
        this.flightCancellationJsonDeserializer = flightCancellationJsonDeserializer;

        this.gsonWithDeserializer = new GsonBuilder().registerTypeAdapter(CancelPassengerEntity.class, this.flightCancellationJsonDeserializer).create();
    }

    public Observable<List<Passenger>> getCancelablePassenger(String invoiceId) {
        return flightApi.getCancellablePassenger(invoiceId)
                .map(new Func1<Response<String>, CancelPassengerEntity>() {
                    @Override
                    public CancelPassengerEntity call(Response<String> stringResponse) {
                        return gsonWithDeserializer.fromJson(stringResponse.body(), CancelPassengerEntity.class);
                    }
                })
                .flatMap(new Func1<CancelPassengerEntity, Observable<List<Passenger>>>() {
                    @Override
                    public Observable<List<Passenger>> call(CancelPassengerEntity cancelPassengerEntity) {
                        flightCancellationReasonDataCacheSource.saveCache(
                                cancelPassengerEntity.getAttributes().getReasons());
                        return Observable.just(cancelPassengerEntity.getAttributes().getPassengers());
                    }
                });
    }

    public Observable<EstimateRefundResultEntity> getEstimateRefund(FlightEstimateRefundRequest request) {
        return flightApi.getEstimateRefund(new DataRequest<>(request))
                .map(new Func1<Response<DataResponse<EstimateRefundResultEntity>>, EstimateRefundResultEntity>() {
                    @Override
                    public EstimateRefundResultEntity call(Response<DataResponse<EstimateRefundResultEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }

    public Observable<CancellationRequestEntity> requestCancellation(DataRequest<FlightCancellationRequestBody> request) {
        return flightApi.requestCancellation(gsonWithDeserializer
                .fromJson(gsonWithDeserializer.toJson(request), JsonElement.class).getAsJsonObject())
                .flatMap(new Func1<Response<DataResponse<CancellationRequestEntity>>, Observable<CancellationRequestEntity>>() {
                    @Override
                    public Observable<CancellationRequestEntity> call(Response<DataResponse<CancellationRequestEntity>> dataResponseResponse) {
                        return Observable.just(dataResponseResponse.body().getData());
                    }
                });
    }

    public Observable<List<Reason>> getCancellationReasons() {
        return flightCancellationReasonDataCacheSource.getCache();
    }
}
