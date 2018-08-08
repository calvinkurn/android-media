package com.tokopedia.flight.dashboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.FlightSelectPassengerFragment;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

public class FlightSelectPassengerActivity extends BaseSimpleActivity implements HasComponent<FlightDashboardComponent>, FlightSelectPassengerFragment.OnFragmentInteractionListener {
    public static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";

    public static Intent getCallingIntent(Activity activity, FlightPassengerViewModel viewModel) {
        Intent intent = new Intent(activity, FlightSelectPassengerActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, viewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        FlightPassengerViewModel passengerPassData = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        return FlightSelectPassengerFragment.newInstance(passengerPassData);
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
    public void actionSavePassenger(FlightPassengerViewModel passData) {
        setIntent(getIntent().putExtra(EXTRA_PASS_DATA, passData));
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
