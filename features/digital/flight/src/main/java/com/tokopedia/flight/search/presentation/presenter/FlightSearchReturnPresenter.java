package com.tokopedia.flight.search.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.data.api.single.response.Route;
import com.tokopedia.flight.search.domain.usecase.FlightSearchJourneyByIdUseCase;
import com.tokopedia.flight.search.presentation.contract.FlightSearchReturnContract;
import com.tokopedia.flight.search.presentation.model.FlightFareViewModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchReturnPresenter extends BaseDaggerPresenter<FlightSearchReturnContract.View>
        implements FlightSearchReturnContract.Presenter {

    private static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private static final int MIN_DIFF_HOURS = 6;
    private FlightSearchJourneyByIdUseCase flightSearchJourneyByIdUseCase;
    private FlightAnalytics flightAnalytics;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightSearchReturnPresenter(FlightSearchJourneyByIdUseCase flightSearchJourneyByIdUseCase,
                                       FlightAnalytics flightAnalytics) {
        this.flightSearchJourneyByIdUseCase = flightSearchJourneyByIdUseCase;
        this.flightAnalytics = flightAnalytics;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel returnJourneyModel) {
        flightSearchJourneyByIdUseCase.execute(flightSearchJourneyByIdUseCase.createRequestParams(selectedFlightDeparture),
                new Subscriber<FlightJourneyViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isViewAttached()) {
                            getView().showErrorPickJourney();
                        }
                    }

                    @Override
                    public void onNext(FlightJourneyViewModel departureJourneyModel) {
                        if (isValidReturnJourney(departureJourneyModel, returnJourneyModel)) {
                            FlightPriceViewModel priceViewModel = getView().getPriceViewModel();
                            priceViewModel.setReturnPrice(buildFare(returnJourneyModel.getFare(), true));
                            priceViewModel.setComboKey(returnJourneyModel.getComboId());

                            getView().navigateToCart(returnJourneyModel, priceViewModel);
                        }
                    }
                });
    }

    @Override
    public void onFlightSearchSelected(String selectedFlightDeparture, String selectedFlightReturn) {
        FlightPriceViewModel priceViewModel = getView().getPriceViewModel();

        compositeSubscription.add(Observable.zip(
                flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase.createRequestParams(selectedFlightDeparture)),
                flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase.createRequestParams(selectedFlightReturn))
                    .doOnNext(new Action1<FlightJourneyViewModel>() {
                        @Override
                        public void call(FlightJourneyViewModel journeyViewModel) {
                            flightAnalytics.eventSearchProductClickFromDetail(getView().getFlightSearchPassData(), journeyViewModel);
                        }
                    }),
                new Func2<FlightJourneyViewModel, FlightJourneyViewModel, Boolean>() {
                    @Override
                    public Boolean call(FlightJourneyViewModel departureJourneyViewModel, FlightJourneyViewModel returnJourneyViewModel) {
                        priceViewModel.setReturnPrice(buildFare(returnJourneyViewModel.getFare(), true));
                        priceViewModel.setComboKey(returnJourneyViewModel.getComboId());

                        return isValidReturnJourney(departureJourneyViewModel, returnJourneyViewModel);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isViewAttached()) {
                            getView().showErrorPickJourney();
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getView().navigateToCart(selectedFlightReturn, priceViewModel);
                        } else {
                            getView().showReturnTimeShouldGreaterThanArrivalDeparture();
                        }
                    }
                })
        );
    }

    @Override
    public void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel returnJourneyModel, int adapterPosition) {
        flightAnalytics.eventSearchProductClickFromList(getView().getFlightSearchPassData(), returnJourneyModel, adapterPosition);
        onFlightSearchSelected(selectedFlightDeparture, returnJourneyModel);
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }

        flightSearchJourneyByIdUseCase.unsubscribe();
    }

    private boolean isValidReturnJourney(FlightJourneyViewModel departureViewModel, FlightJourneyViewModel returnViewModel) {
        if (departureViewModel.getRouteList() != null && returnViewModel.getRouteList() != null) {
            if (departureViewModel.getRouteList().size() > 0 && returnViewModel.getRouteList().size() > 0) {
                Route lastDepartureRoute = departureViewModel.getRouteList().get(departureViewModel.getRouteList().size() - 1);
                Route firstReturnRoute = returnViewModel.getRouteList().get(0);
                Date departureArrivalTime = FlightDateUtil.stringToDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, lastDepartureRoute.getArrivalTimestamp());
                Date returnDepartureTime = FlightDateUtil.stringToDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, firstReturnRoute.getDepartureTimestamp());
                long different = returnDepartureTime.getTime() - departureArrivalTime.getTime();
                if (different >= 0) {
                    long hours = different / ONE_HOUR;
                    CommonUtils.dumper("diff : " + hours);
                    return hours >= MIN_DIFF_HOURS;
                } else {
                    return false;
                }

            }
        }
        return true;
    }

    private FlightFareViewModel buildFare(FlightFareViewModel journeyFare, boolean isNeedCombo) {
        FlightFareViewModel flightFareViewModel;
        if (isNeedCombo) {
            flightFareViewModel = new FlightFareViewModel(
                    journeyFare.getAdult(),
                    journeyFare.getAdultCombo(),
                    journeyFare.getChild(),
                    journeyFare.getChildCombo(),
                    journeyFare.getInfant(),
                    journeyFare.getInfantCombo(),
                    journeyFare.getAdultNumeric(),
                    journeyFare.getAdultNumericCombo(),
                    journeyFare.getChildNumeric(),
                    journeyFare.getChildNumericCombo(),
                    journeyFare.getInfantNumeric(),
                    journeyFare.getInfantNumericCombo()
            );
        } else {
            flightFareViewModel = new FlightFareViewModel(
                    journeyFare.getAdult(),
                    "",
                    journeyFare.getChild(),
                    "",
                    journeyFare.getInfant(),
                    "",
                    journeyFare.getAdultNumeric(),
                    0,
                    journeyFare.getChildNumeric(),
                    0,
                    journeyFare.getInfantNumeric(),
                    0
            );
        }

        return flightFareViewModel;
    }
}
