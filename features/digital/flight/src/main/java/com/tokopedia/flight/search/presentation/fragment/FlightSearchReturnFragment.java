package com.tokopedia.flight.search.presentation.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.search.presentation.contract.FlightSearchReturnContract;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeAllResultViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeOnlyBestPairingViewModel;
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.presentation.presenter.FlightSearchReturnPresenter;
import com.tokopedia.common.travel.widget.DepartureTripLabelView;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.flight.search.presentation.activity.FlightSearchActivity.EXTRA_PASS_DATA;
import static com.tokopedia.flight.search.presentation.activity.FlightSearchReturnActivity.EXTRA_DEPARTURE_ID;
import static com.tokopedia.flight.search.presentation.activity.FlightSearchReturnActivity.EXTRA_IS_BEST_PAIRING;
import static com.tokopedia.flight.search.presentation.activity.FlightSearchReturnActivity.EXTRA_PRICE_VIEW_MODEL;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchReturnFragment extends FlightSearchFragment
        implements FlightSearchReturnContract.View {

    @Inject
    FlightSearchReturnPresenter flightSearchReturnPresenter;

    private DepartureTripLabelView departureHeaderLabel;
    private String selectedFlightDeparture;
    private boolean isBestPairing = false;
    private boolean isViewOnlyBestPairing = false;

    private FlightPriceViewModel priceViewModel;

    public static Fragment newInstance(FlightSearchPassDataViewModel passDataViewModel,
                                       String selectedDepartureID, boolean bestPairing,
                                       FlightPriceViewModel priceViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        args.putString(EXTRA_DEPARTURE_ID, selectedDepartureID);
        args.putBoolean(EXTRA_IS_BEST_PAIRING, bestPairing);
        args.putParcelable(EXTRA_PRICE_VIEW_MODEL, priceViewModel);

        FlightSearchReturnFragment fragment = new FlightSearchReturnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = (savedInstanceState != null) ? savedInstanceState : getArguments();

        selectedFlightDeparture = arguments.getString(EXTRA_DEPARTURE_ID);
        isBestPairing = arguments.getBoolean(EXTRA_IS_BEST_PAIRING);
        isViewOnlyBestPairing = arguments.getBoolean(EXTRA_IS_BEST_PAIRING);
        priceViewModel = arguments.getParcelable(EXTRA_PRICE_VIEW_MODEL);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_search_return;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        departureHeaderLabel = view.findViewById(R.id.departure_trip_label);

        flightSearchReturnPresenter.attachView(this);

        clearAdapterData();
        flightSearchPresenter.getDetailDepartureFlight(selectedFlightDeparture);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        outState.putString(EXTRA_DEPARTURE_ID, selectedFlightDeparture);
        outState.putBoolean(EXTRA_IS_BEST_PAIRING, isBestPairing);
        outState.putParcelable(EXTRA_PRICE_VIEW_MODEL, priceViewModel);
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
            departureHeaderLabel.setValueName(getString(R.string.flight_label_multi_maskapai));
        } else if (flightJourneyViewModel.getAirlineDataList() != null &&
                flightJourneyViewModel.getAirlineDataList().size() == 1) {
            departureHeaderLabel.setValueName(flightJourneyViewModel.getAirlineDataList().get(0).getShortName());
        }
        if (flightJourneyViewModel.getAddDayArrival() > 0) {
            departureHeaderLabel.setValueDepartureTime(String.format("| %s - %s (+%sh)", flightJourneyViewModel.getDepartureTime(),
                    flightJourneyViewModel.getArrivalTime(), String.valueOf(flightJourneyViewModel.getAddDayArrival())));
        } else {
            departureHeaderLabel.setValueDepartureTime(String.format("| %s - %s", flightJourneyViewModel.getDepartureTime(),
                    flightJourneyViewModel.getArrivalTime()));
        }

        departureHeaderLabel.setValueTitle(String.format("%s - %s",
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
    public void navigateToCart(FlightJourneyViewModel journeyViewModel, FlightPriceViewModel flightPriceViewModel) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(journeyViewModel.getId(), flightPriceViewModel, false);
        }
    }

    @Override
    public void navigateToCart(String selectedFlightReturn, FlightPriceViewModel flightPriceViewModel) {
        if (onFlightSearchFragmentListener != null) {
            onFlightSearchFragmentListener.selectFlight(selectedFlightReturn, flightPriceViewModel, false);
        }
    }

    @Override
    public void showErrorPickJourney() {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                getString(R.string.flight_error_pick_journey));
    }

    @Override
    public void showSeeAllResultView() {
        getAdapter().addElement(new FlightSearchSeeAllResultViewModel());
        isViewOnlyBestPairing = true;
    }

    @Override
    public void showSeeBestPairingResultView() {
        getAdapter().addElement(new FlightSearchSeeOnlyBestPairingViewModel());
        isViewOnlyBestPairing = false;
    }

    @Override
    public boolean isOnlyShowBestPair() {
        return isViewOnlyBestPairing;
    }

    @Override
    public FlightPriceViewModel getPriceViewModel() {
        return priceViewModel;
    }

    protected void onSelectedFromDetail(String selectedId) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, selectedId);
    }

    @Override
    protected FlightFilterModel buildFilterModel(FlightFilterModel flightFilterModel) {
        FlightFilterModel filterModel = flightFilterModel;
        filterModel.setBestPairing(isViewOnlyBestPairing);
        filterModel.setJourneyId(selectedFlightDeparture);
        filterModel.setReturn(isReturning());

        return filterModel;
    }

    @Override
    public void onDestroyView() {
        flightSearchReturnPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void renderSearchList(List<FlightJourneyViewModel> list, boolean needRefresh) {

        if (isBestPairing && !isOnlyShowBestPair()) {
            showSeeBestPairingResultView();
        }

        super.renderSearchList(list, needRefresh);

        if (isDoneLoadData() && list.size() > 0 && isOnlyShowBestPair()) {
            showSeeAllResultView();
        }
    }

    @Override
    public void onShowAllClicked() {
        super.onShowAllClicked();

        showSeeAllResultDialog(priceViewModel.getDeparturePrice().getAdultCombo(),
                priceViewModel.getDeparturePrice().getAdult());
    }

    @Override
    public void onShowBestPairingClicked() {
        super.onShowBestPairingClicked();

        showSeeBestPairingDialog(priceViewModel.getDeparturePrice().getAdult(), priceViewModel.getDeparturePrice().getAdultCombo());
    }

    private void showSeeAllResultDialog(String bestPairPrice, String normalPrice) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.flight_search_choose_except_best_pairing_dialog_title));
        dialog.setDesc(MethodChecker.fromHtml(
                getString(R.string.flight_search_choose_except_best_pairing_dialog_description, bestPairPrice, normalPrice)));
        dialog.setBtnOk(getString(R.string.flight_search_dialog_proceed_button_text));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterModel().setBestPairing(false);
                isViewOnlyBestPairing = false;
                flightSearchPresenter.fetchSortAndFilterLocalData(selectedSortOption, getFilterModel(), false);
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel(getString(R.string.flight_search_return_dialog_abort));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showSeeBestPairingDialog(String normalPrice, String bestPairPrice) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.flight_search_choose_best_pairing_dialog_title));
        dialog.setDesc(MethodChecker.fromHtml(
                getString(R.string.flight_search_choose_best_pairing_dialog_description, normalPrice, bestPairPrice)));
        dialog.setBtnOk(getString(R.string.flight_search_dialog_proceed_button_text));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterModel().setBestPairing(true);
                isViewOnlyBestPairing = true;
                flightSearchPresenter.fetchSortAndFilterLocalData(selectedSortOption, getFilterModel(), false);
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel(getString(R.string.flight_search_return_dialog_abort));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
