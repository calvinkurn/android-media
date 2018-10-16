package com.tokopedia.flight.searchV2.presentation.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchReturnContract;
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.searchV2.presentation.model.FlightPriceViewModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.searchV2.presentation.presenter.FlightSearchReturnPresenter;

import javax.inject.Inject;

import static com.tokopedia.flight.searchV2.presentation.activity.FlightSearchActivity.EXTRA_PASS_DATA;
import static com.tokopedia.flight.searchV2.presentation.activity.FlightSearchReturnActivity.EXTRA_DEPARTURE_ID;
import static com.tokopedia.flight.searchV2.presentation.activity.FlightSearchReturnActivity.EXTRA_IS_BEST_PAIRING;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchReturnFragment extends FlightSearchFragment
        implements FlightSearchReturnContract.View {

    @Inject
    FlightSearchReturnPresenter flightSearchReturnPresenter;

    private TextView departureHeaderLabel;
    private TextView airlineName;
    private TextView duration;
    private String selectedFlightDeparture;
    private boolean isBestPairing = false;

    public static Fragment newInstance(FlightSearchPassDataViewModel passDataViewModel,
                                       String selectedDepartureID, boolean bestPairing) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        args.putString(EXTRA_DEPARTURE_ID, selectedDepartureID);
        args.putBoolean(EXTRA_IS_BEST_PAIRING, bestPairing);

        FlightSearchReturnFragment fragment = new FlightSearchReturnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        selectedFlightDeparture = getArguments().getString(EXTRA_DEPARTURE_ID);
        isBestPairing = getArguments().getBoolean(EXTRA_IS_BEST_PAIRING);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_search_return;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        airlineName = view.findViewById(R.id.airline_name);
        duration = view.findViewById(R.id.duration);
        departureHeaderLabel = view.findViewById(R.id.tv_departure_header_card_label);

        clearAdapterData();
        flightSearchPresenter.getDetailDepartureFlight(selectedFlightDeparture);

        super.onViewCreated(view, savedInstanceState);
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
    protected FlightAirportViewModel getDepartureAirport() {
        return passDataViewModel.getArrivalAirport();
    }

    @Override
    protected FlightAirportViewModel getArrivalAirport() {
        return passDataViewModel.getDepartureAirport();
    }

    @Override
    public boolean isReturning() {
        return true;
    }

    @Override
    public void onSuccessGetDetailFlightDeparture(FlightJourneyViewModel flightJourneyViewModel) {
        if (flightJourneyViewModel.getAirlineDataList() != null &&
                flightJourneyViewModel.getAirlineDataList().size() > 1) {
            airlineName.setText(getString(R.string.flight_label_multi_maskapai));
        } else if (flightJourneyViewModel.getAirlineDataList() != null &&
                flightJourneyViewModel.getAirlineDataList().size() == 1) {
            airlineName.setText(flightJourneyViewModel.getAirlineDataList().get(0).getName());
        }
        if (flightJourneyViewModel.getAddDayArrival() > 0) {
            duration.setText(String.format("| %s - %s (+%sh)", flightJourneyViewModel.getDepartureTime(),
                    flightJourneyViewModel.getArrivalTime(), String.valueOf(flightJourneyViewModel.getAddDayArrival())));
        } else {
            duration.setText(String.format("| %s - %s", flightJourneyViewModel.getDepartureTime(),
                    flightJourneyViewModel.getArrivalTime()));
        }

        departureHeaderLabel.setText(String.format("%s - %s",
                getString(R.string.flight_label_departure_flight),
                FlightDateUtil.formatToUi(passDataViewModel.getDepartureDate())));
    }

    @Override
    public void onItemClicked(FlightJourneyViewModel journeyViewModel) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, journeyViewModel);
    }

    @Override
    public void onItemClicked(FlightJourneyViewModel journeyViewModel, int adapterPosition) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, journeyViewModel, adapterPosition);
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
    public void navigateToCart(FlightJourneyViewModel journeyViewModel) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(journeyViewModel.getId(), new FlightPriceViewModel());
        }
    }

    @Override
    public void navigateToCart(String selectedFlightReturn) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedFlightReturn, new FlightPriceViewModel());
        }
    }

    @Override
    public void showErrorPickJourney() {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                getString(R.string.flight_error_pick_journey));
    }

    protected void onSelectedFromDetail(String selectedId) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, selectedId);
    }

    @Override
    protected FlightFilterModel buildFilterModel() {
        FlightFilterModel filterModel = new FlightFilterModel();
        filterModel.setBestPairing(isBestPairing);
        filterModel.setJourneyId(selectedFlightDeparture);

        return filterModel;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        flightSearchReturnPresenter.onDestroy();
    }
}
