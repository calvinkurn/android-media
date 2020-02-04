package com.tokopedia.flight.dashboard.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by furqan on 23/01/18.
 */

public class GetFlightClassByIdUseCase extends UseCase<FlightClassEntity> {
    private static final String PARAM_CLASS_ID = "PARAM_CLASS_ID";
    private static final int DEFAULT_CLASS_ID = 1;

    private FlightRepository flightRepository;

    @Inject
    public GetFlightClassByIdUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightClassEntity> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightClassById(requestParams.getInt(PARAM_CLASS_ID, DEFAULT_CLASS_ID));
    }

    public RequestParams createRequestParams(int classId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_CLASS_ID, classId);
        return requestParams;
    }

}
