package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.searchV2.domain.FlightSearchJourneyByIdUseCase;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchReturnContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightFareViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightPriceViewModel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchReturnPresenter extends BaseDaggerPresenter<FlightSearchReturnContract.View>
        implements FlightSearchReturnContract.Presenter {

    private static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private FlightSearchJourneyByIdUseCase flightSearchJourneyByIdUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightSearchReturnPresenter(FlightSearchJourneyByIdUseCase flightSearchJourneyByIdUseCase) {
        this.flightSearchJourneyByIdUseCase = flightSearchJourneyByIdUseCase;
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
                flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase.createRequestParams(selectedFlightReturn)),
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
                Date departureArrivalTime = FlightDateUtil.stringToDate(FlightDateUtil.FORMAT_DATE_API, lastDepartureRoute.getArrivalTimestamp());
                Date returnDepartureTime = FlightDateUtil.stringToDate(FlightDateUtil.FORMAT_DATE_API, firstReturnRoute.getDepartureTimestamp());
                long different = returnDepartureTime.getTime() - departureArrivalTime.getTime();
                if (different >= 0) {
                    long hours = different / ONE_HOUR;
                    CommonUtils.dumper("diff : " + hours);
                    return hours >= 6;
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
