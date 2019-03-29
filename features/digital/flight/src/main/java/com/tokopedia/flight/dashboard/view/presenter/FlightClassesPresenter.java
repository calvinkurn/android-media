package com.tokopedia.flight.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.dashboard.domain.GetFlightClassesUseCase;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.mapper.FlightClassViewModelMapper;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassesPresenter extends BaseDaggerPresenter<FlightClassesContract.View> implements FlightClassesContract.Presenter {

    private GetFlightClassesUseCase getFlightClassesUseCase;
    private FlightClassViewModelMapper flightClassViewModelMapper;

    @Inject
    public FlightClassesPresenter(GetFlightClassesUseCase getFlightClassesUseCase,
                                  FlightClassViewModelMapper flightClassViewModelMapper) {
        this.getFlightClassesUseCase = getFlightClassesUseCase;
        this.flightClassViewModelMapper = flightClassViewModelMapper;
    }

    @Override
    public void actionFetchClasses() {
        getView().showFetchClassesLoading();
        getFlightClassesUseCase.execute(getFlightClassesUseCase.createRequestParam(), new Subscriber<List<FlightClassEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideFetchClassesLoading();
                }
            }

            @Override
            public void onNext(List<FlightClassEntity> flightClassEntities) {
                if (isViewAttached()) {
                    getView().hideFetchClassesLoading();
                    getView().renderFlightClasses(flightClassViewModelMapper.transform(flightClassEntities));
                }
            }
        });
    }
}
