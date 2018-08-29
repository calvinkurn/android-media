package com.tokopedia.flight.cancellation.data.cloud;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancelPassengerEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;

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
    private Gson gson;

    @Inject
    public FlightCancellationCloudDataSource(FlightApi flightApi, @FlightQualifier Gson gson) {
        this.flightApi = flightApi;
        this.gson = gson;
    }

    public Observable<List<Passenger>> getCancelablePassenger(String invoiceId) {
        return flightApi.getCancellablePassenger(invoiceId)
                .flatMap(new Func1<Response<DataResponse<CancelPassengerEntity>>, Observable<List<Passenger>>>() {
                    @Override
                    public Observable<List<Passenger>> call(Response<DataResponse<CancelPassengerEntity>> dataResponse) {
                        return Observable.just(dataResponse.body().getData()
                                .getAttributes().getPassengers());
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
        return flightApi.requestCancellation(gson
                .fromJson(gson.toJson(request), JsonElement.class).getAsJsonObject())
                .flatMap(new Func1<Response<DataResponse<CancellationRequestEntity>>, Observable<CancellationRequestEntity>>() {
                    @Override
                    public Observable<CancellationRequestEntity> call(Response<DataResponse<CancellationRequestEntity>> dataResponseResponse) {
                        return Observable.just(dataResponseResponse.body().getData());
                    }
                });
    }
}
