package com.tokopedia.flight.searchV2.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity;
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightFlowUtil;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.presentation.fragment.FlightSearchFragment;
import com.tokopedia.flight.searchV2.presentation.fragment.FlightSearchReturnFragment;

public class FlightSearchReturnActivity extends FlightSearchActivity
        implements FlightSearchFragment.OnFlightSearchFragmentListener {

    public static final String EXTRA_DEPARTURE_ID = "EXTRA_DEPARTURE_ID";
    private static final int REQUEST_CODE_BOOKING = 13;

    private String selectedDepartureID;

    public static Intent getCallingIntent(Context context,
                                          FlightSearchPassDataViewModel passDataViewModel,
                                          String selectedDepartureID) {
        Intent intent = new Intent(context, FlightSearchReturnActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, passDataViewModel);
        intent.putExtra(EXTRA_DEPARTURE_ID, selectedDepartureID);
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
        return FlightSearchReturnFragment.newInstance(passDataViewModel, selectedDepartureID);
    }

    @Override
    protected FlightAirportViewModel getArrivalAirport() {
        return passDataViewModel.getDepartureAirport();
    }

    @Override
    public void selectFlight(String selectedFlightID) {
        // TODO : PASS FLIGHTPRICEVIEWMODEL
        startActivityForResult(FlightBookingActivity
                        .getCallingIntent(this,
                                passDataViewModel,
                                selectedDepartureID,
                                selectedFlightID,
                                null),
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
