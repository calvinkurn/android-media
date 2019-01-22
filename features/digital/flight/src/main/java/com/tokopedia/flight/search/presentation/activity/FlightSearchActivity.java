package com.tokopedia.flight.search.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity;
import com.tokopedia.flight.common.constant.FlightFlowConstant;
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightFlowUtil;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;

public class FlightSearchActivity extends BaseFlightActivity
        implements FlightSearchFragment.OnFlightSearchFragmentListener {

    public static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final int REQUEST_CODE_BOOKING = 10;
    private static final int REQUEST_CODE_RETURN = 11;
    protected String dateString;
    protected String passengerString;
    protected String classString;
    protected FlightSearchPassDataViewModel passDataViewModel;

    public static Intent getCallingIntent(Context context, FlightSearchPassDataViewModel passDataViewModel) {
        Intent intent = new Intent(context, FlightSearchActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, passDataViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeDataFromExtras();
        super.onCreate(savedInstanceState);

        setupSearchToolbar();
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchFragment.newInstance(passDataViewModel);
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.SEARCH;
    }

    private void initializeDataFromExtras() {
        passDataViewModel = getIntent().getExtras().getParcelable(EXTRA_PASS_DATA);
        initializeToolbarData();
    }

    protected void initializeToolbarData() {
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.getDepartureDate()
        );
        passengerString = buildPassengerTextFormatted(passDataViewModel.getFlightPassengerViewModel());
        classString = passDataViewModel.getFlightClass().getTitle();
    }

    protected String buildPassengerTextFormatted(FlightPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " " + getString(R.string.flight_dashboard_adult_passenger);
            if (passData.getChildren() > 0) {
                passengerFmt += ", " + passData.getChildren() + " " + getString(R.string.flight_dashboard_adult_children);
            }
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " " + getString(R.string.flight_dashboard_adult_infant);
            }
        }
        return passengerFmt;
    }

    protected FlightAirportViewModel getDepartureAirport() {
        return passDataViewModel.getDepartureAirport();
    }

    protected FlightAirportViewModel getArrivalAirport() {
        return passDataViewModel.getArrivalAirport();
    }

    private void setupSearchToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String title = getDepartureAirport().getCityName() + " ➝ " + getArrivalAirport().getCityName();
        String subtitle = dateString + " | " + passengerString + " | " + classString;
        updateTitle(title, subtitle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RETURN:
            case REQUEST_CODE_BOOKING:
                if (data != null) {
                    switch (data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0)) {
                        case FlightFlowConstant.PRICE_CHANGE:
                            Fragment fragment = getFragment();
                            if (fragment instanceof FlightSearchFragment) {
                                ((FlightSearchFragment) fragment).flightSearchPresenter
                                        .attachView((FlightSearchFragment) fragment);
                                ((FlightSearchFragment) fragment).loadInitialData();
                            }
                            break;
                        case FlightFlowConstant.EXPIRED_JOURNEY:
                            FlightFlowUtil.actionSetResultAndClose(this,
                                    getIntent(),
                                    FlightFlowConstant.EXPIRED_JOURNEY
                            );
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void selectFlight(String selectedFlightID, FlightPriceViewModel flightPriceViewModel, boolean isBestPairing) {
        if (passDataViewModel.isOneWay()) {
            startActivityForResult(FlightBookingActivity
                            .getCallingIntent(this, passDataViewModel, selectedFlightID, flightPriceViewModel),
                    REQUEST_CODE_BOOKING);
        } else {
            startActivityForResult(FlightSearchReturnActivity
                            .getCallingIntent(this, passDataViewModel, selectedFlightID, isBestPairing, flightPriceViewModel),
                    REQUEST_CODE_RETURN);
        }
    }

    @Override
    public void changeDate(FlightSearchPassDataViewModel flightSearchPassDataViewModel) {
        this.passDataViewModel = flightSearchPassDataViewModel;
        initializeToolbarData();
        setupSearchToolbar();
    }
}
