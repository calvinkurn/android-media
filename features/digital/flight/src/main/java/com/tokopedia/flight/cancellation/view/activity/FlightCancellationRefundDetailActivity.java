package com.tokopedia.flight.cancellation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.Menu;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationRefundDetailFragment;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

/**
 * Created by alvarisi on 4/9/18.
 */

public class FlightCancellationRefundDetailActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {
    private static final String FLIGHT_WRAPPER_EXTRA = "FLIGHT_WRAPPER_EXTRA";
    private static final String FLIGHT_STEPS_NUMBER_EXTRA = "FLIGHT_STEPS_NUMBER_EXTRA";
    private static final int DEFAULT_STEPS_NUMBER = 3;

    private FlightCancellationComponent cancellationComponent;

    private FlightCancellationWrapperViewModel wrapperViewModel;
    private int stepsNumber;

    public static Intent getCallingIntent(Activity activity, FlightCancellationWrapperViewModel wrapperViewModel,
                                          int stepsNumber) {
        Intent intent = new Intent(activity, FlightCancellationRefundDetailActivity.class);
        intent.putExtra(FLIGHT_WRAPPER_EXTRA, wrapperViewModel);
        intent.putExtra(FLIGHT_STEPS_NUMBER_EXTRA, stepsNumber);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        wrapperViewModel = getIntent().getParcelableExtra(FLIGHT_WRAPPER_EXTRA);
        stepsNumber = getIntent().getIntExtra(FLIGHT_STEPS_NUMBER_EXTRA, DEFAULT_STEPS_NUMBER);
        super.onCreate(savedInstanceState);
        setupToolbar();
    }

    @Override
    public FlightCancellationComponent getComponent() {
        if (cancellationComponent == null) {
            initInjector();
        }
        return cancellationComponent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightCancellationRefundDetailFragment.newInstance(wrapperViewModel, stepsNumber);
    }

    private void initInjector() {
        cancellationComponent = DaggerFlightCancellationComponent.builder()
                .flightComponent(getFlightComponent())
                .build();
    }

    private void setupToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.grey_500));
        String title = getString(com.tokopedia.flight.R.string.activity_label_flight_cancellation);
        String subtitle = String.format(
                getString(com.tokopedia.flight.R.string.flight_cancellation_subtitle_order_id),
                wrapperViewModel.getInvoice()
        );
        updateTitle(title, subtitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
