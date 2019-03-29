package com.tokopedia.flight.passenger.data.cloud;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.passenger.data.cloud.entity.PassengerListEntity;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.flight.search.data.api.single.response.FlightDataResponse;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightPassengerDataListCloudSource extends DataListCloudSource<PassengerListEntity> {

    private FlightApi flightApi;
    private Gson gson;

    @Inject
    public FlightPassengerDataListCloudSource(FlightApi flightApi, @FlightQualifier Gson gson) {
        this.flightApi = flightApi;
        this.gson = gson;
    }

    @Override
    public Observable<List<PassengerListEntity>> getData(HashMap<String, Object> params) {
        return this.flightApi.getSavedPassengerData()
                .flatMap(new Func1<Response<FlightDataResponse<List<PassengerListEntity>>>, Observable<List<PassengerListEntity>>>() {
                    @Override
                    public Observable<List<PassengerListEntity>> call(Response<FlightDataResponse<List<PassengerListEntity>>> flightDataResponseResponse) {
                        return Observable.just(flightDataResponseResponse.body().getData());
                    }
                });
    }

    public Observable<Response<Object>> deletePassenger(DeletePassengerRequest request, String idempotencyKey) {
        return this.flightApi.deleteSavedPassengerData(new DataRequest<>(request), idempotencyKey);
    }

    public Observable<Response<FlightDataResponse<PassengerListEntity>>> updatePassenger(UpdatePassengerRequest request, String idempotencyKey) {
        return this.flightApi.updatePassengerListData(new DataRequest<>(request), idempotencyKey);
    }
}
