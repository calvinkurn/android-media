package com.tokopedia.flight.passenger.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.view.Menu;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.passenger.di.DaggerFlightPassengerComponent;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightPassengerListFragment;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightPassengerListActivity extends BaseFlightActivity implements HasComponent<FlightPassengerComponent> {

    public static Intent createIntent(Context context, FlightBookingPassengerViewModel selected,
                                      String requestId, String departureDate, boolean isDomestic) {
        Intent intent = new Intent(context, FlightPassengerListActivity.class);
        intent.putExtra(FlightPassengerListFragment.EXTRA_SELECTED_PASSENGER, selected);
        intent.putExtra(FlightPassengerListFragment.EXTRA_REQUEST_ID, requestId);
        intent.putExtra(FlightPassengerListFragment.EXTRA_DEPARTURE_DATE, departureDate);
        intent.putExtra(FlightPassengerListFragment.EXTRA_IS_DOMESTIC, isDomestic);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getIntent()
                .getParcelableExtra(FlightPassengerListFragment.EXTRA_SELECTED_PASSENGER);
        String requestId = getIntent().getStringExtra(
                FlightPassengerListFragment.EXTRA_REQUEST_ID);
        String departureDate = getIntent().getStringExtra(
                FlightPassengerListFragment.EXTRA_DEPARTURE_DATE);
        boolean isDomestic = getIntent().getBooleanExtra(
                FlightPassengerListFragment.EXTRA_IS_DOMESTIC, false);
        return FlightPassengerListFragment.createInstance(flightBookingPassengerViewModel,
                requestId, departureDate, isDomestic);
    }

    @Override
    public FlightPassengerComponent getComponent() {
        return DaggerFlightPassengerComponent.builder()
                .flightComponent(getFlightComponent())
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
