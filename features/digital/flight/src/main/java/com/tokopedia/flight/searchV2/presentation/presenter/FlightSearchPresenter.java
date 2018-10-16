package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.common.constant.FlightErrorConstant;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.subscriber.OnNextSubscriber;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.FlightAirlineHardRefreshUseCase;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.domain.FlightDeleteFlightSearchReturnDataUseCase;
import com.tokopedia.flight.searchV2.domain.FlightSearchCombinedUseCase;
import com.tokopedia.flight.searchV2.domain.FlightSearchV2UseCase;
import com.tokopedia.flight.searchV2.domain.FlightSortAndFilterUseCase;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModelList;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightRouteModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
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

    private FlightSearchV2UseCase flightSearchV2UseCase;
    private FlightSortAndFilterUseCase flightSortAndFilterUseCase;
    private FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase;
    private FlightSearchCombinedUseCase flightSearchCombinedUseCase;
    private FlightDeleteFlightSearchReturnDataUseCase flightDeleteFlightSearchReturnDataUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightSearchPresenter(FlightSearchV2UseCase flightSearchV2UseCase,
                                 FlightSortAndFilterUseCase flightSortAndFilterUseCase,
                                 FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase, FlightSearchCombinedUseCase flightSearchCombinedUseCase,
                                 FlightDeleteFlightSearchReturnDataUseCase flightDeleteFlightSearchReturnDataUseCase) {
        this.flightSearchV2UseCase = flightSearchV2UseCase;
        this.flightSortAndFilterUseCase = flightSortAndFilterUseCase;
        this.flightAirlineHardRefreshUseCase = flightAirlineHardRefreshUseCase;
        this.flightSearchCombinedUseCase = flightSearchCombinedUseCase;
        this.flightDeleteFlightSearchReturnDataUseCase = flightDeleteFlightSearchReturnDataUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize() {
        if (!getView().isReturning()) {
            flightAirlineHardRefreshUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    if (isViewAttached()) {
                        getView().showGetListError(e);
                    }
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    getView().setNeedRefreshAirline(false);
//                    getView().fetchFlightSearchData();
                }
            });
        } else {
//            getView().fetchFlightSearchData();
        }
    }

    @Override
    public void onSeeDetailItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        deleteFlightReturnSearch(journeyViewModel.getId());
    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel) {
        deleteFlightReturnSearch(journeyViewModel.getId());
    }

    @Override
    public void onSearchItemClicked(String selectedId) {
        deleteFlightReturnSearch(selectedId);
    }

    @Override
    public void onSuccessDateChanged(int year, int month, int dayOfMonth) {

    }

    @Override
    public void setDelayHorizontalProgress() {

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

        boolean anyLoadToCloud = false;

        for (int i = 0, sizei = flightAirportCombineModelList.getData().size(); i < sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = flightAirportCombineModelList.getData().get(i);
            boolean needLoadFromCloud = true;

            if (flightAirportCombineModel.isHasLoad() && !flightAirportCombineModel.isNeedRefresh()) {
                needLoadFromCloud = false;
            }

            if (needLoadFromCloud) {
                anyLoadToCloud = true;
                fetchSearchDataFromCloud(passDataViewModel, flightAirportCombineModel);
            }
        }
        if (!anyLoadToCloud) {
//            reloadDataFromCache();
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
                flightAirportCombineModel.getAirlines());

        flightSearchV2UseCase.execute(flightSearchV2UseCase.createRequestParams(
                requestModel,
                getView().isReturning(),
                !passDataViewModel.isOneWay(),
                ""),
                new Subscriber<FlightSearchMetaViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        if (isViewAttached()) {
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
                        getView().onGetSearchMeta(flightSearchMetaViewModel);
                    }
                });
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
    public void fetchSortAndFilterLocalData(@FlightSortOption int flightSortOption, FlightFilterModel flightFilterModel, boolean needRefresh) {
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
                        getView().renderSearchList(flightJourneyViewModels, needRefresh);

                        if (getView().isDoneLoadData()) {
                            getView().addBottomPaddingForSortAndFilterActionButton();
                            getView().addToolbarElevation();
                            getView().hideHorizontalProgress();
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

        super.detachView();
    }

    private void deleteFlightReturnSearch(String selectedId) {
        /*flightDeleteFlightSearchReturnDataUseCase.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    getView().navigateToNextPage(selectedId);
                }
            }
        });*/
        getView().navigateToNextPage(selectedId);
    }

    private void addSubscription(Subscription subscription) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }
}
