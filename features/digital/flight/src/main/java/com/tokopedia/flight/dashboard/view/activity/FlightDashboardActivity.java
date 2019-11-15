package com.tokopedia.flight.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.bookingV3.presentation.activity.FlightBookingActivity;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.FlightDashboardFragment;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardActivity extends BaseFlightActivity implements HasComponent<FlightDashboardComponent> {

    private static final String EXTRA_TRIP = "dest";
    private static final String EXTRA_ADULT = "a";
    private static final String EXTRA_CHILD = "c";
    private static final String EXTRA_INFANT = "i";
    private static final String EXTRA_CLASS = "s";
    private static final String EXTRA_AUTO_SEARCH = "auto_search";

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, FlightDashboardActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent().hasExtra(EXTRA_TRIP) && getIntent().hasExtra(EXTRA_ADULT) && getIntent().hasExtra(EXTRA_CHILD)
                && getIntent().hasExtra(EXTRA_INFANT) && getIntent().hasExtra(EXTRA_CLASS) && getIntent().hasExtra(EXTRA_AUTO_SEARCH)) {
            return FlightDashboardFragment.getInstance(
                    getIntent().getStringExtra(EXTRA_TRIP),
                    getIntent().getStringExtra(EXTRA_ADULT),
                    getIntent().getStringExtra(EXTRA_CHILD),
                    getIntent().getStringExtra(EXTRA_INFANT),
                    getIntent().getStringExtra(EXTRA_CLASS),
                    getIntent().getStringExtra(EXTRA_AUTO_SEARCH)
            );
        } else {
            return FlightDashboardFragment.getInstance();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    @Override
    public FlightDashboardComponent getComponent() {
        return DaggerFlightDashboardComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.HOMEPAGE;
    }
}