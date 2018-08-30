package com.tokopedia.flight.search.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.travel.widget.DepartureTripLabelView;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.presenter.FlightSearchReturnPresenter;
import com.tokopedia.flight.search.view.FlightSearchReturnView;
import com.tokopedia.flight.search.view.activity.FlightSearchReturnActivity;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import javax.inject.Inject;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchReturnFragment extends FlightSearchFragment implements FlightSearchReturnView {

    @Inject
    FlightSearchReturnPresenter flightSearchReturnPresenter;
    private String selectedFlightDeparture;
    private DepartureTripLabelView departureTripLabelView;


    public static FlightSearchReturnFragment newInstance(FlightSearchPassDataViewModel passDataViewModel, String selectedDepartureID) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        args.putString(FlightSearchReturnActivity.EXTRA_SEL_DEPARTURE_ID, selectedDepartureID);
        FlightSearchReturnFragment fragment = new FlightSearchReturnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedFlightDeparture = getArguments().getString(FlightSearchReturnActivity.EXTRA_SEL_DEPARTURE_ID);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_search_return;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        departureTripLabelView = view.findViewById(R.id.departure_trip_label);

        flightSearchPresenter.getDetailDepartureFlight(selectedFlightDeparture);
    }

    protected FlightAirportViewModel getDepartureAirport() {
        return flightSearchPassDataViewModel.getArrivalAirport();
    }

    protected FlightAirportViewModel getArrivalAirport() {
        return flightSearchPassDataViewModel.getDepartureAirport();
    }

    @Override
    public boolean isReturning() {
        return true;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessGetDetailFlightDeparture(FlightSearchViewModel flightSearchViewModel) {
        if (flightSearchViewModel.getAirlineList().size() > 1) {
            departureTripLabelView.setValueName(getString(R.string.flight_label_multi_maskapai));
        } else if (flightSearchViewModel.getAirlineList().size() == 1) {
            departureTripLabelView.setValueName(flightSearchViewModel.getAirlineList().get(0).getName());
        }
        if (flightSearchViewModel.getAddDayArrival() > 0) {
            departureTripLabelView.setValueDepartureTime(String.format(" | %s - %s (+%sh)", flightSearchViewModel.getDepartureTime(),
                    flightSearchViewModel.getArrivalTime(), String.valueOf(flightSearchViewModel.getAddDayArrival())));
        } else {
            departureTripLabelView.setValueDepartureTime(String.format(" | %s - %s", flightSearchViewModel.getDepartureTime(),
                    flightSearchViewModel.getArrivalTime()));
        }

        departureTripLabelView.setValueTitle(String.format("%s - %s", getString(R.string.flight_label_departure_flight), FlightDateUtil.formatToUi(flightSearchPassDataViewModel.getDepartureDate())));
    }

    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, flightSearchViewModel);
    }


    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel, int adapterPosition) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, flightSearchViewModel, adapterPosition);
    }

    @Override
    protected final void initInjector() {
        super.initInjector();
        if (flightSearchComponent == null) {
            flightSearchComponent =
                    DaggerFlightSearchComponent.builder()
                            .flightComponent(FlightComponentInstance.getFlightComponent(getActivity().getApplication()))
                            .build();
        }
        flightSearchComponent
                .inject(this);
        flightSearchReturnPresenter.attachView(this);
    }

    @Override
    public void showReturnTimeShouldGreaterThanArrivalDeparture() {
        if (isAdded()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(R.string.flight_search_return_departure_should_greater_message);
            dialog.setPositiveButton(getActivity().getString(R.string.title_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setCancelable(false);
            dialog.create().show();
        }
    }

    @Override
    public void navigateToCart(FlightSearchViewModel returnFlightSearchViewModel) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(returnFlightSearchViewModel.getId());
        }
    }

    @Override
    public void navigateToCart(String selectedFlightReturn) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedFlightReturn);
        }
    }

    @Override
    public void showErrorPickJourney() {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(R.string.flight_error_pick_journey));
    }

    protected void onSelectedFromDetail(String selectedId) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, selectedId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        flightSearchReturnPresenter.onDestroy();
    }
}
