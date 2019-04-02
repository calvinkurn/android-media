package com.tokopedia.flight.search.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity;
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightFlowUtil;
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.presentation.fragment.FlightSearchReturnFragment;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;

public class FlightSearchReturnActivity extends FlightSearchActivity
        implements FlightSearchFragment.OnFlightSearchFragmentListener {

    public static final String EXTRA_DEPARTURE_ID = "EXTRA_DEPARTURE_ID";
    private static final int REQUEST_CODE_BOOKING = 13;
    public static final String EXTRA_IS_BEST_PAIRING = "EXTRA_IS_BEST_PAIRING";
    public static final String EXTRA_PRICE_VIEW_MODEL = "EXTRA_PRICE_VIEW_MODEL";

    private String selectedDepartureID;

    public static Intent getCallingIntent(Context context,
                                          FlightSearchPassDataViewModel passDataViewModel,
                                          String selectedDepartureID, boolean isBestPairing,
                                          FlightPriceViewModel priceViewModel) {
        Intent intent = new Intent(context, FlightSearchReturnActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, passDataViewModel);
        intent.putExtra(EXTRA_DEPARTURE_ID, selectedDepartureID);
        intent.putExtra(EXTRA_IS_BEST_PAIRING, isBestPairing);
        intent.putExtra(EXTRA_PRICE_VIEW_MODEL, priceViewModel);
        return intent;
    }

    @Override
    protected void initializeToolbarData() {
        selectedDepartureID = getIntent().getStringExtra(EXTRA_DEPARTURE_ID);

        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.getReturnDate()
        );
        passengerString = buildPassengerTextFormatted(passDataViewModel.getFlightPassengerViewModel());
        classString = passDataViewModel.getFlightClass().getTitle();
    }

    @Override
    protected FlightAirportViewModel getDepartureAirport() {
        return passDataViewModel.getArrivalAirport();
    }

    @Override
    protected Fragment getNewFragment() {
        FlightPriceViewModel priceViewModel = getIntent().getParcelableExtra(EXTRA_PRICE_VIEW_MODEL);
        return FlightSearchReturnFragment.newInstance(passDataViewModel, selectedDepartureID,
                getIntent().getBooleanExtra(EXTRA_IS_BEST_PAIRING, false),
                priceViewModel);
    }

    @Override
    protected FlightAirportViewModel getArrivalAirport() {
        return passDataViewModel.getDepartureAirport();
    }

    @Override
    public void selectFlight(String selectedFlightID, FlightPriceViewModel flightPriceViewModel, boolean isBestPairing) {
        startActivityForResult(FlightBookingActivity
                        .getCallingIntent(this,
                                passDataViewModel,
                                selectedDepartureID,
                                selectedFlightID,
                                flightPriceViewModel),
                REQUEST_CODE_BOOKING);
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.SEARCH_RETURN;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_BOOKING:
                if (data != null) {
                    FlightFlowUtil.actionSetResultAndClose(this,
                            getIntent(),
                            data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0)
                    );
                }
                break;
        }
    }
}
