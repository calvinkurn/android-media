package com.tokopedia.flight.search.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.constant.TravelSortOption;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.constant.FlightErrorConstant;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.subscriber.OnNextSubscriber;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightRequestUtil;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.domain.usecase.FlightDeleteAllFlightSearchDataUseCase;
import com.tokopedia.flight.search.domain.usecase.FlightDeleteFlightSearchReturnDataUseCase;
import com.tokopedia.flight.search.domain.usecase.FlightSearchCombinedUseCase;
import com.tokopedia.flight.search.domain.usecase.FlightSearchJourneyByIdUseCase;
import com.tokopedia.flight.search.domain.usecase.FlightSearchV2UseCase;
import com.tokopedia.flight.search.domain.usecase.FlightSortAndFilterUseCase;
import com.tokopedia.flight.search.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.presentation.model.FlightAirportCombineModel;
import com.tokopedia.flight.search.presentation.model.FlightAirportCombineModelList;
import com.tokopedia.flight.search.presentation.model.FlightFareViewModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;
import com.tokopedia.flight.search.presentation.model.FlightRouteModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchCombinedApiRequestModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchContract.View>
        implements FlightSearchContract.Presenter {

    public static final int DELAY_HORIZONTAL_PROGRESS = 500;

    private FlightSearchV2UseCase flightSearchV2UseCase;
    private FlightSortAndFilterUseCase flightSortAndFilterUseCase;
    private FlightSearchCombinedUseCase flightSearchCombinedUseCase;
    private FlightDeleteAllFlightSearchDataUseCase flightDeleteAllFlightSearchDataUseCase;
    private FlightDeleteFlightSearchReturnDataUseCase flightDeleteFlightSearchReturnDataUseCase;
    private FlightSearchJourneyByIdUseCase flightSearchJourneyByIdUseCase;
    private FlightAnalytics flightAnalytics;
    private CompositeSubscription compositeSubscription;

    private int maxCall = 0;
    private int callCounter = 0;

    @Inject
    public FlightSearchPresenter(FlightSearchV2UseCase flightSearchV2UseCase,
                                 FlightSortAndFilterUseCase flightSortAndFilterUseCase,
                                 FlightSearchCombinedUseCase flightSearchCombinedUseCase,
                                 FlightDeleteAllFlightSearchDataUseCase flightDeleteAllFlightSearchDataUseCase,
                                 FlightDeleteFlightSearchReturnDataUseCase flightDeleteFlightSearchReturnDataUseCase,
                                 FlightSearchJourneyByIdUseCase flightSearchJourneyByIdUseCase,
                                 FlightAnalytics flightAnalytics) {
        this.flightSearchV2UseCase = flightSearchV2UseCase;
        this.flightSortAndFilterUseCase = flightSortAndFilterUseCase;
        this.flightSearchCombinedUseCase = flightSearchCombinedUseCase;
        this.flightDeleteAllFlightSearchDataUseCase = flightDeleteAllFlightSearchDataUseCase;
        this.flightDeleteFlightSearchReturnDataUseCase = flightDeleteFlightSearchReturnDataUseCase;
        this.flightSearchJourneyByIdUseCase = flightSearchJourneyByIdUseCase;
        this.flightAnalytics = flightAnalytics;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize() {}

    @Override
    public void onSeeDetailItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        flightAnalytics.eventSearchDetailClick(journeyViewModel, adapterPosition);
        flightAnalytics.eventProductDetailImpression(journeyViewModel, adapterPosition);
    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        flightAnalytics.eventSearchProductClickFromList(getView().getFlightSearchPassData(), journeyViewModel, adapterPosition);
        deleteFlightReturnSearch(getDeleteFlightReturnSubscriber(journeyViewModel));
    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel) {
        flightAnalytics.eventSearchProductClickFromList(getView().getFlightSearchPassData(), journeyViewModel);
        deleteFlightReturnSearch(getDeleteFlightReturnSubscriber(journeyViewModel));
    }

    @Override
    public void onSearchItemClicked(String selectedId) {
        flightSearchJourneyByIdUseCase.execute(flightSearchJourneyByIdUseCase.createRequestParams(selectedId),
                new Subscriber<FlightJourneyViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(FlightJourneyViewModel journeyViewModel) {
                        flightAnalytics.eventSearchProductClickFromDetail(getView().getFlightSearchPassData(), journeyViewModel);
                        deleteFlightReturnSearch(getDeleteFlightReturnSubscriber(journeyViewModel));
                    }
                });
    }

    @Override
    public void onSuccessDateChanged(int year, int month, int dayOfMonth) {
        FlightSearchPassDataViewModel flightSearchPassDataViewModel = getView().getFlightSearchPassData();
        Calendar calendar = FlightDateUtil.getCurrentCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, dayOfMonth);
        Date dateToSet = calendar.getTime();
        Date twoYears = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);

        if (dateToSet.after(twoYears)) {
            getView().showDepartureDateMaxTwoYears(R.string.flight_dashboard_departure_max_two_years_from_today_error);
        } else if (!getView().isReturning() && dateToSet.before(FlightDateUtil.getCurrentDate())) {
            getView().showDepartureDateShouldAtLeastToday(R.string.flight_dashboard_departure_should_atleast_today_error);
        } else if (getView().isReturning() && dateToSet.before(FlightDateUtil.stringToDate(flightSearchPassDataViewModel.getDepartureDate()))) {
            getView().showReturnDateShouldGreaterOrEqual(R.string.flight_dashboard_return_should_greater_equal_error);
        } else {
            String dateString = FlightDateUtil.dateToString(dateToSet, FlightDateUtil.DEFAULT_FORMAT);

            if (getView().isReturning()) {
                flightSearchPassDataViewModel.setReturnDate(dateString);
                deleteFlightReturnSearch(getDeleteFlightAfterChangeDateSubscriber());
            } else {
                flightSearchPassDataViewModel.setDepartureDate(dateString);
                deleteAllSearchData(getDeleteFlightAfterChangeDateSubscriber());
            }
            getView().setFlightSearchPassData(flightSearchPassDataViewModel);
        }
    }

    @Override
    public void setDelayHorizontalProgress() {
        Subscription subscription = Observable.timer(DELAY_HORIZONTAL_PROGRESS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnNextSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        getView().hideHorizontalProgress();
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getDetailDepartureFlight(String journeyId) {
        flightSearchJourneyByIdUseCase.execute(flightSearchJourneyByIdUseCase.createRequestParams(journeyId),
                new Subscriber<FlightJourneyViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FlightJourneyViewModel journeyViewModel) {
                        getView().onSuccessGetDetailFlightDeparture(journeyViewModel);
                    }
                });
    }

    @Override
    public void fetchCombineData(FlightSearchPassDataViewModel passDataViewModel) {
        if (!passDataViewModel.isOneWay()) {
            FlightPassengerViewModel flightPassengerViewModel = passDataViewModel.getFlightPassengerViewModel();
            int adult = flightPassengerViewModel.getAdult();
            int child = flightPassengerViewModel.getChildren();
            int infant = flightPassengerViewModel.getInfant();
            int classID = passDataViewModel.getFlightClass().getId();

            FlightSearchCombinedApiRequestModel combinedRequestModel = null;

            String depAirport, arrAirport;

            depAirport = passDataViewModel.getDepartureAirport().getAirportCode();
            if (depAirport == null || depAirport.equals("")) {
                depAirport = passDataViewModel.getDepartureAirport().getCityCode();
            }

            arrAirport = passDataViewModel.getArrivalAirport().getAirportCode();
            if (arrAirport == null || arrAirport.equals("")) {
                arrAirport = passDataViewModel.getArrivalAirport().getCityCode();
            }

            List<FlightRouteModel> routes = new ArrayList<>();
            routes.add(new FlightRouteModel(depAirport, arrAirport, passDataViewModel.getDate(false)));
            routes.add(new FlightRouteModel(arrAirport, depAirport, passDataViewModel.getDate(true)));

            combinedRequestModel = new FlightSearchCombinedApiRequestModel(routes, adult, child, infant, classID);

            flightSearchCombinedUseCase.execute(
                    flightSearchCombinedUseCase.createRequestParam(combinedRequestModel),
                    new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached()) {
                                getView().fetchFlightSearchData();
                            }
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (isViewAttached()) {
                                getView().fetchFlightSearchData();
                            }
                        }
                    });
        } else {
            if (isViewAttached()) {
                getView().fetchFlightSearchData();
            }
        }
    }

    @Override
    public void fetchSearchData(FlightSearchPassDataViewModel passDataViewModel,
                                FlightAirportCombineModelList flightAirportCombineModelList) {
        if (isViewAttached()) {
            getView().removeToolbarElevation();
        }

        maxCall = flightAirportCombineModelList.getData().size();

        for (int i = 0, sizei = flightAirportCombineModelList.getData().size(); i < sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = flightAirportCombineModelList.getData().get(i);
            boolean needLoadFromCloud = true;

            if (flightAirportCombineModel.isHasLoad() && !flightAirportCombineModel.isNeedRefresh()) {
                needLoadFromCloud = false;
            }

            if (needLoadFromCloud) {
                fetchSearchDataFromCloud(passDataViewModel, flightAirportCombineModel);
            }
        }
    }

    @Override
    public void fetchSearchDataFromCloud(FlightSearchPassDataViewModel passDataViewModel,
                                         FlightAirportCombineModel flightAirportCombineModel) {

        String date = passDataViewModel.getDate(getView().isReturning());
        FlightPassengerViewModel flightPassengerViewModel = passDataViewModel.getFlightPassengerViewModel();
        int adult = flightPassengerViewModel.getAdult();
        int child = flightPassengerViewModel.getChildren();
        int infant = flightPassengerViewModel.getInfant();
        int classID = passDataViewModel.getFlightClass().getId();

        FlightSearchApiRequestModel requestModel = new FlightSearchApiRequestModel(
                flightAirportCombineModel.getDepAirport(),
                flightAirportCombineModel.getArrAirport(),
                date, adult, child, infant, classID,
                flightAirportCombineModel.getAirlines(),
                FlightRequestUtil.getLocalIpAddress());

        flightSearchV2UseCase.execute(flightSearchV2UseCase.createRequestParams(
                requestModel,
                getView().isReturning(),
                !passDataViewModel.isOneWay(),
                getView().getFilterModel().getJourneyId()),
                new Subscriber<FlightSearchMetaViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callCounter++;
                        getView().addProgress(countProgress());
                        if (isViewAttached() && isDoneLoadData()) {
                            if (e instanceof FlightException) {
                                List<FlightError> errors = ((FlightException) e).getErrorList();
                                if (errors.contains(new FlightError(FlightErrorConstant.FLIGHT_ROUTE_NOT_FOUND))) {
                                    getView().showNoRouteFlightEmptyState(e.getMessage());
                                    return;
                                }
                            }
                            getView().showGetListError(e);
                        }
                    }

                    @Override
                    public void onNext(FlightSearchMetaViewModel flightSearchMetaViewModel) {
                        if (!flightSearchMetaViewModel.isNeedRefresh()) {
                            callCounter++;
                        }

                        getView().onGetSearchMeta(flightSearchMetaViewModel);
                    }
                });
    }

    private int countProgress() {
        return FlightSearchFragment.MAX_PROGRESS / maxCall;
    }

    @Override
    public void fetchSearchDataFromCloudWithDelay(FlightSearchPassDataViewModel passDataViewModel,
                                                  FlightAirportCombineModel flightAirportCombineModelList,
                                                  int delayInSecond) {
        getView().removeToolbarElevation();
        Subscription subscription = Observable.timer(delayInSecond, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnNextSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        fetchSearchDataFromCloud(passDataViewModel, flightAirportCombineModelList);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void fetchSortAndFilterLocalData(@TravelSortOption int flightSortOption, FlightFilterModel flightFilterModel, boolean needRefresh) {
        flightSortAndFilterUseCase.execute(
                flightSortAndFilterUseCase.createRequestParams(flightSortOption, flightFilterModel),
                new Subscriber<List<FlightJourneyViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<FlightJourneyViewModel> flightJourneyViewModels) {
                        if (!needRefresh || flightJourneyViewModels.size() > 0) {
                            getView().clearAdapterData();
                        }

                        getView().renderSearchList(flightJourneyViewModels, needRefresh);

                        if (getView().isDoneLoadData()) {
                            getView().addBottomPaddingForSortAndFilterActionButton();
                            getView().addToolbarElevation();
                        }
                    }
                }
        );
    }

    @Override
    public void detachView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }

        flightSearchJourneyByIdUseCase.unsubscribe();
        flightDeleteFlightSearchReturnDataUseCase.unsubscribe();
        flightSearchCombinedUseCase.unsubscribe();
        flightSortAndFilterUseCase.unsubscribe();
        flightSearchV2UseCase.unsubscribe();

        super.detachView();
    }

    @Override
    public boolean isDoneLoadData() {
        return callCounter >= maxCall;
    }

    @Override
    public void resetCounterCall() {
        callCounter = 0;
    }

    private void deleteFlightReturnSearch(Subscriber subscriber) {
        flightDeleteFlightSearchReturnDataUseCase.execute(subscriber);
    }

    private void deleteAllSearchData(Subscriber subscriber) {
        flightDeleteAllFlightSearchDataUseCase.execute(subscriber);
    }

    private Subscriber<Boolean> getDeleteFlightAfterChangeDateSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().onErrorDeleteFlightCache(throwable);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessDeleteFlightCache();
            }
        };
    }

    private Subscriber<Boolean> getDeleteFlightReturnSubscriber(FlightJourneyViewModel journeyViewModel) {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().onErrorDeleteFlightCache(throwable);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                FlightPriceViewModel priceViewModel = new FlightPriceViewModel();
                priceViewModel.setDeparturePrice(buildFare(journeyViewModel.getFare(),
                        !getView().getFlightSearchPassData().isOneWay()));
                getView().navigateToNextPage(journeyViewModel.getId(), priceViewModel,
                        journeyViewModel.isBestPairing());
            }
        };
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

    private void addSubscription(Subscription subscription) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }
}
