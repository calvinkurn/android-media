package com.tokopedia.flight.passenger.data;

import com.tokopedia.flight.passenger.data.cloud.FlightPassengerDataListCloudSource;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.flight.passenger.data.db.FlightPassengerDao;
import com.tokopedia.flight.passenger.data.db.FlightPassengerTable;
import com.tokopedia.flight.search.data.db.mapper.FlightPassengerMapper;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nabillasabbaha on 12/03/19.
 */

public class FlightPassengerFactorySource {

    FlightPassengerDao flightPassengerDao;
    FlightPassengerDataListCloudSource flightPassengerDataListCloudSource;
    FlightPassengerMapper flightPassengerMapper;

    @Inject
    public FlightPassengerFactorySource(FlightPassengerDao flightPassengerDao,
                                        FlightPassengerDataListCloudSource flightPassengerDataListCloudSource,
                                        FlightPassengerMapper flightPassengerMapper) {
        this.flightPassengerDao = flightPassengerDao;
        this.flightPassengerDataListCloudSource = flightPassengerDataListCloudSource;
        this.flightPassengerMapper = flightPassengerMapper;
    }

    public Observable<List<FlightPassengerTable>> getPassengerList(String passengerId) {
        return Observable.just(passengerId)
                .map(it -> {
                    if (it.equals("")) {
                        return flightPassengerDao.findAllFlightPassenger();
                    } else {
                        return flightPassengerDao.findPassengerByIdPassenger(it);
                    }
                })
                .flatMap(it -> {
                    if (it.isEmpty()) return getPassengerListFromCloud();
                    else return Observable.just(it);
                });
    }

    public Observable<Boolean> updateIsSelected(String passengerId, int isSelected) {
        return Observable.just(true)
                .map(it -> flightPassengerDao.updateFlightPassenger(passengerId, isSelected))
                .map(it -> it > 0);
    }

    public Observable<Boolean> deleteAllListPassenger() {
        return Observable.just(true)
                .map(it -> flightPassengerDao.deleteAll())
                .map(it -> it > 0);
    }

    public Observable<Boolean> deletePassenger(final DeletePassengerRequest deletePassengerRequest, String idempotencyKey) {
        return flightPassengerDataListCloudSource.deletePassenger(deletePassengerRequest, idempotencyKey)
                .flatMap(objectResponse -> Observable.just(deletePassengerRequest)
                        .map(it -> flightPassengerDao.deletePassengerById(it.getId()))
                        .map(it -> it > 0));
    }

    public Observable<Boolean> updatePassenger(final UpdatePassengerRequest updatePassengerRequest, String idempotencyKey) {
        return flightPassengerDataListCloudSource.updatePassenger(updatePassengerRequest, idempotencyKey)
                .flatMap(savedPassengerResponse -> Observable.just(savedPassengerResponse)
                        .map(it -> flightPassengerMapper.mapToFlightPassengerDb(it.body().getData()))
                        .map(it -> flightPassengerDao.update(it))
                        .map(it -> it > 0));
    }

    private Observable<List<FlightPassengerTable>> getPassengerListFromCloud() {
        return Observable.just(true)
                .map(it -> flightPassengerDao.deleteAll())
                .flatMap(it -> flightPassengerDataListCloudSource.getData(null))
                .map(it -> flightPassengerMapper.mapListEntityToTable(it))
                .map(it -> flightPassengerDao.insertAll(it))
                .map(it -> flightPassengerDao.findAllFlightPassenger());

    }
}
