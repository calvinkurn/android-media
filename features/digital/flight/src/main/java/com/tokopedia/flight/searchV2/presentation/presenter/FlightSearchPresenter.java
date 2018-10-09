package com.tokopedia.flight.searchV2.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.domain.FlightAirlineHardRefreshUseCase;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.domain.FlightSearchV2UseCase;
import com.tokopedia.flight.searchV2.domain.FlightSortAndFilterUseCase;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightAirportCombineModelList;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightRouteModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchContract.View>
        implements FlightSearchContract.Presenter {

    private FlightSearchV2UseCase flightSearchV2UseCase;
    private FlightSortAndFilterUseCase flightSortAndFilterUseCase;
    private FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchV2UseCase flightSearchV2UseCase,
                                 FlightSortAndFilterUseCase flightSortAndFilterUseCase,
                                 FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase) {
        this.flightSearchV2UseCase = flightSearchV2UseCase;
        this.flightSortAndFilterUseCase = flightSortAndFilterUseCase;
        this.flightAirlineHardRefreshUseCase = flightAirlineHardRefreshUseCase;
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
                    getView().fetchFlightSearchData();
                }
            });
        } else {
            getView().fetchFlightSearchData();
        }
    }

    @Override
    public void onSeeDetailItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {

    }

    @Override
    public void onSearchItemClicked(FlightJourneyViewModel journeyViewModel) {

    }

    @Override
    public void onSuccessDateChanged(int year, int month, int dayOfMonth) {

    }

    @Override
    public void setDelayHorizontalProgress() {

    }

    @Override
    public void fetchSearchData(FlightSearchPassDataViewModel passDataViewModel,
                                FlightAirportCombineModelList flightAirportCombineModelList) {
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
        FlightSearchCombinedApiRequestModel combinedRequestModel = null;

        if (!passDataViewModel.isOneWay()) {
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
        }

        flightSearchV2UseCase.execute(flightSearchV2UseCase.createRequestParams(
                requestModel,
                combinedRequestModel,
                getView().isReturning(),
                !passDataViewModel.isOneWay()),
                new Subscriber<FlightSearchMetaViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FlightSearchMetaViewModel flightSearchMetaViewModel) {
                        Log.d("DATA", flightSearchMetaViewModel.isNeedRefresh() + " + " + flightSearchMetaViewModel.getMaxRetry());
                        getView().onGetSearchMeta(flightSearchMetaViewModel);
                    }
                });
    }
}
