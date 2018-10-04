package com.tokopedia.flight.searchV2.presentation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.view.model.AirportCombineModelList;
import com.tokopedia.flight.search.view.model.FlightAirportCombineModel;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.di.DaggerFlightSearchV2Component;
import com.tokopedia.flight.searchV2.di.FlightSearchV2Component;
import com.tokopedia.flight.searchV2.presentation.model.FlightRouteModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel;
import com.tokopedia.flight.searchV2.presentation.presenter.FlightSearchTestingContract;
import com.tokopedia.flight.searchV2.presentation.presenter.FlightSearchTestingPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightSearchTestingFragment extends BaseDaggerFragment
        implements FlightSearchTestingContract.View {

    protected static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";

    protected FlightSearchPassDataViewModel flightSearchPassDataViewModel;
    private AirportCombineModelList airportCombineModelList;

    @Inject
    FlightSearchTestingPresenter flightSearchTestingPresenter;

    public static Fragment newInstance(FlightSearchPassDataViewModel passDataViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        Fragment fragment = new FlightSearchTestingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_search_testing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            flightSearchPassDataViewModel = getArguments().getParcelable(EXTRA_PASS_DATA);
        }

        flightSearchTestingPresenter.attachView(this);
        actionFetchFlightSearchData();

    }

    public void actionFetchFlightSearchData() {
        setUpCombinationAirport();
        String date = flightSearchPassDataViewModel.getDate(false);
        FlightPassengerViewModel flightPassengerViewModel = flightSearchPassDataViewModel.getFlightPassengerViewModel();
        int adult = flightPassengerViewModel.getAdult();
        int child = flightPassengerViewModel.getChildren();
        int infant = flightPassengerViewModel.getInfant();
        int classID = flightSearchPassDataViewModel.getFlightClass().getId();
        for (int i = 0, sizei = airportCombineModelList.getData().size(); i < sizei; i++) {
            FlightAirportCombineModel flightAirportCombineModel = airportCombineModelList.getData().get(i);
            boolean needLoadFromCloud = true;
            if (flightAirportCombineModel.isHasLoad() && !flightAirportCombineModel.isNeedRefresh()) {
                needLoadFromCloud = false;
            }
            if (needLoadFromCloud) {
                FlightSearchApiRequestModel flightSearchSingleApiRequestModel = new FlightSearchApiRequestModel(
                        flightAirportCombineModel.getDepAirport(), flightAirportCombineModel.getArrAirport(),
                        date, adult, child, infant, classID, flightAirportCombineModel.getAirlines());
                List<FlightRouteModel> flightRouteModels = new ArrayList<>();
                flightRouteModels.add(new FlightRouteModel(
                        flightAirportCombineModel.getDepAirport(),
                        flightAirportCombineModel.getArrAirport(),
                        date));
                flightRouteModels.add(new FlightRouteModel(
                        flightAirportCombineModel.getArrAirport(),
                        flightAirportCombineModel.getDepAirport(),
                        flightSearchPassDataViewModel.getReturnDate()));
                FlightSearchCombinedApiRequestModel flightSearchCombinedApiRequestModel = new FlightSearchCombinedApiRequestModel(
                        flightRouteModels, adult, child, infant, classID
                );
                flightSearchTestingPresenter.searchFlight(flightSearchSingleApiRequestModel,
                        flightSearchCombinedApiRequestModel,
                        flightSearchPassDataViewModel.isOneWay(),
                        false);
            }
        }
    }

    private void setUpCombinationAirport() {
        List<String> departureAirportList;
        String depAirportID = getDepartureAirport().getAirportCode();
        if (TextUtils.isEmpty(depAirportID)) {
            String[] depAirportIDs = getDepartureAirport().getCityAirports();
            departureAirportList = Arrays.asList(depAirportIDs);
        } else {
            departureAirportList = new ArrayList<>();
            departureAirportList.add(depAirportID);
        }

        List<String> arrivalAirportList;
        String arrAirportID = getArrivalAirport().getAirportCode();
        if (TextUtils.isEmpty(arrAirportID)) {
            String[] arrAirportIDs = getArrivalAirport().getCityAirports();
            arrivalAirportList = Arrays.asList(arrAirportIDs);
        } else {
            arrivalAirportList = new ArrayList<>();
            arrivalAirportList.add(arrAirportID);
        }

        airportCombineModelList = new AirportCombineModelList(departureAirportList, arrivalAirportList);
    }

    protected FlightAirportViewModel getDepartureAirport() {
        return flightSearchPassDataViewModel.getDepartureAirport();
    }

    protected FlightAirportViewModel getArrivalAirport() {
        return flightSearchPassDataViewModel.getArrivalAirport();
    }

    @Override
    protected void initInjector() {
        FlightSearchV2Component flightSearchV2Component = DaggerFlightSearchV2Component.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getActivity().getApplication()))
                .build();

        flightSearchV2Component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

}