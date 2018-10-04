package com.tokopedia.flight.searchV2.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.searchV2.domain.FlightSearchV2UseCase;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightSearchTestingPresenter extends BaseDaggerPresenter<FlightSearchTestingContract.View>
        implements FlightSearchTestingContract.Presenter {

    private final String TAG = FlightSearchTestingPresenter.class.getSimpleName();

    private FlightSearchV2UseCase flightSearchV2UseCase;

    @Inject
    public FlightSearchTestingPresenter(FlightSearchV2UseCase flightSearchV2UseCase) {
        this.flightSearchV2UseCase = flightSearchV2UseCase;
    }

    @Override
    public void searchFlight(FlightSearchApiRequestModel flightSearchApiRequestModel,
                             FlightSearchCombinedApiRequestModel flightSearchCombinedApiRequestModel,
                             boolean oneWay, boolean isReturning) {
        flightSearchV2UseCase.execute(
                flightSearchV2UseCase.createRequestParams(
                        flightSearchApiRequestModel, flightSearchCombinedApiRequestModel,
                        isReturning, !oneWay),
                new Subscriber<FlightSearchMetaViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(FlightSearchMetaViewModel flightSearchMetaViewModel) {
                        Log.d(TAG, flightSearchMetaViewModel.getArrivalAirport());
                    }
                }
        );
    }
}