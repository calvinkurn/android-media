package com.tokopedia.flight.passenger.data;

import com.tokopedia.flight.passenger.data.cloud.FlightPassengerDataListCloudSource;
import com.tokopedia.flight.passenger.data.cloud.entity.PassengerListEntity;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.flight.passenger.data.db.FlightPassengerDataListDbSource;
import com.tokopedia.flight.search.data.api.single.response.FlightDataResponse;
import com.tokopedia.flight_dbflow.FlightPassengerDB;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 28/02/18.
 */

public class FlightPassengerFactorySource {

    FlightPassengerDataListDbSource flightPassengerDataListDbSource;
    FlightPassengerDataListCloudSource flightPassengerDataListCloudSource;

    @Inject
    public FlightPassengerFactorySource(FlightPassengerDataListDbSource flightPassengerDataListDbSource,
                                        FlightPassengerDataListCloudSource flightPassengerDataListCloudSource) {
        this.flightPassengerDataListDbSource = flightPassengerDataListDbSource;
        this.flightPassengerDataListCloudSource = flightPassengerDataListCloudSource;
    }

    public Observable<List<FlightPassengerDB>> getPassengerList(String passengerId) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(FlightPassengerDataListDbSource.PASSENGER_ID, passengerId);

        return flightPassengerDataListDbSource.isDataAvailable()
                .flatMap(new Func1<Boolean, Observable<List<FlightPassengerDB>>>() {
                    @Override
                    public Observable<List<FlightPassengerDB>> call(Boolean aBoolean) {
                        if (aBoolean) {
                            return flightPassengerDataListDbSource.getData(params);
                        } else {
                            return getPassengerListFromCloud();
                        }
                    }
                });
    }

    public Observable<FlightPassengerDB> getSinglePassenger(String passengerId) {
        return flightPassengerDataListDbSource.getSingleData(passengerId);
    }

    public Observable<Boolean> updateIsSelected(String passengerId, int isSelected) {
        return flightPassengerDataListDbSource.updateIsSelected(passengerId, isSelected);
    }

    public Observable<Boolean> deleteAllListPassenger() {
        return flightPassengerDataListDbSource.deleteAll();
    }

    public Observable<Boolean> deletePassenger(final DeletePassengerRequest deletePassengerRequest, String idempotencyKey) {
        return flightPassengerDataListCloudSource.deletePassenger(deletePassengerRequest, idempotencyKey)
                .flatMap(new Func1<Response<Object>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<Object> objectResponse) {
                        return flightPassengerDataListDbSource.deletePassenger(deletePassengerRequest.getId());
                    }
                });
    }

    public Observable<Boolean> updatePassenger(final UpdatePassengerRequest updatePassengerRequest, String idempotencyKey) {
        return flightPassengerDataListCloudSource.updatePassenger(updatePassengerRequest, idempotencyKey)
                .flatMap(new Func1<Response<FlightDataResponse<PassengerListEntity>>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Response<FlightDataResponse<PassengerListEntity>> savedPassengerResponse) {
                        return flightPassengerDataListDbSource.updatePassengerData(updatePassengerRequest.getPassengerId(),
                                savedPassengerResponse.body().getData());
                    }
                });
    }

    private Observable<List<FlightPassengerDB>> getPassengerListFromCloud() {
        return flightPassengerDataListDbSource.deleteAll()
                .flatMap(new Func1<Boolean, Observable<List<PassengerListEntity>>>() {
                    @Override
                    public Observable<List<PassengerListEntity>> call(Boolean aBoolean) {
                        return flightPassengerDataListCloudSource.getData(null);
                    }
                })
                .flatMap(new Func1<List<PassengerListEntity>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<PassengerListEntity> savedPassengerEntities) {
                        return flightPassengerDataListDbSource.insertAll(savedPassengerEntities);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<List<FlightPassengerDB>>>() {
                    @Override
                    public Observable<List<FlightPassengerDB>> call(Boolean aBoolean) {
                        return flightPassengerDataListDbSource.getData(null);
                    }
                });

    }
}
