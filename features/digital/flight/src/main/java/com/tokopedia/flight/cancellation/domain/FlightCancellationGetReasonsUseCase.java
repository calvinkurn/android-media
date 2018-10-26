package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.domain.mapper.FlightCancellationReasonsViewModelMapper;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 26/10/18.
 */

public class FlightCancellationGetReasonsUseCase extends UseCase<List<FlightCancellationReasonViewModel>> {

    private FlightRepository flightRepository;
    private FlightCancellationReasonsViewModelMapper flightCancellationReasonsViewModelMapper;

    @Inject
    public FlightCancellationGetReasonsUseCase(FlightRepository flightRepository,
                                               FlightCancellationReasonsViewModelMapper flightCancellationReasonsViewModelMapper) {
        this.flightRepository = flightRepository;
        this.flightCancellationReasonsViewModelMapper = flightCancellationReasonsViewModelMapper;
    }

    @Override
    public Observable<List<FlightCancellationReasonViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getCancellationReasons()
                .flatMap(new Func1<List<Reason>, Observable<List<FlightCancellationReasonViewModel>>>() {
                    @Override
                    public Observable<List<FlightCancellationReasonViewModel>> call(List<Reason> reasonList) {
                        return Observable.just(flightCancellationReasonsViewModelMapper.transform(reasonList));
                    }
                });
    }
}
