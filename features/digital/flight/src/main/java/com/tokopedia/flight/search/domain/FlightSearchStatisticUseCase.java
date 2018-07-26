package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class FlightSearchStatisticUseCase extends UseCase<FlightSearchStatisticModel> {
    private FlightSearchUseCase flightSearchUseCase;

    @Inject
    public FlightSearchStatisticUseCase(FlightSearchUseCase flightSearchUseCase) {
        this.flightSearchUseCase = flightSearchUseCase;
    }

    @Override
    public Observable<FlightSearchStatisticModel> createObservable(final RequestParams requestParams) {
        return flightSearchUseCase.createObservable(requestParams).flatMap(new Func1<List<FlightSearchViewModel>, Observable<FlightSearchStatisticModel>>() {
            @Override
            public Observable<FlightSearchStatisticModel> call(List<FlightSearchViewModel> flightSearchViewModelList) {
                return Observable.just(new FlightSearchStatisticModel(flightSearchViewModelList));
            }
        });
    }

    public static RequestParams generateRequestParams(boolean isReturning) {
        return FlightSearchParamUtil.generateRequestParams(null,
                isReturning, true, null, FlightSortOption.NO_PREFERENCE);
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        flightSearchUseCase.unsubscribe();
    }
}
