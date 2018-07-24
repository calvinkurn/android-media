package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.search.domain.mapper.FlightSortMapper;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightSortUseCase extends UseCase<List<FlightSearchViewModel>> {
    private FlightSortMapper flightSortMapper;
    private List<FlightSearchViewModel> flightSearchViewModelList;

    @Inject
    public FlightSortUseCase(FlightSortMapper flightSortMapper) {
        this.flightSortMapper = flightSortMapper;
    }

    public FlightSortUseCase withList(List<FlightSearchViewModel> flightSearchViewModelList){
        this.flightSearchViewModelList = flightSearchViewModelList;
        return this;
    }

    @Override
    public Observable<List<FlightSearchViewModel>> createObservable(final RequestParams requestParams) {
        return Observable.just(flightSearchViewModelList)
                .map(flightSortMapper.withSortOptionId(FlightSearchParamUtil.getSortOptionId(requestParams)));
    }

}
