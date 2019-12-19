package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.Menu;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationDetailFragment;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

import static com.tokopedia.flight.cancellation.view.fragment.FlightCancellationDetailFragment.EXTRA_CANCELLATION_DETAIL_PASS_DATA;

public class FlightCancellationDetailActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {

    FlightCancellationListViewModel flightCancellationListViewModel;

    public static Intent createIntent(Context context,
                                      FlightCancellationListViewModel cancellationListViewModel) {
        Intent intent = new Intent(context, FlightCancellationDetailActivity.class);
        intent.putExtra(EXTRA_CANCELLATION_DETAIL_PASS_DATA, cancellationListViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flightCancellationListViewModel = getIntent()
                .getExtras().getParcelable(EXTRA_CANCELLATION_DETAIL_PASS_DATA);

        setupToolbar();
    }

    @Override
    public FlightCancellationComponent getComponent() {
        return DaggerFlightCancellationComponent.builder()
                .flightComponent(getFlightComponent())
                .build();
    }

    @Override
    protected Fragment getNewFragment() {
        flightCancellationListViewModel = getIntent()
                .getExtras().getParcelable(EXTRA_CANCELLATION_DETAIL_PASS_DATA);
        return new FlightCancellationDetailFragment().createInstance(flightCancellationListViewModel);
    }

    private void setupToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.grey_500));
        String title = getString(com.tokopedia.flight.R.string.flight_cancellation_list_title);
        String subtitle = String.format(
                getString(com.tokopedia.flight.R.string.flight_cancellation_list_id),
                Long.toString(flightCancellationListViewModel.getCancellations().getRefundId())
        );
        updateTitle(title, subtitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
