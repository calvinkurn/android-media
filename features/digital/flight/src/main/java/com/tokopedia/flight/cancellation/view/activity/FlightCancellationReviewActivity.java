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
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationReviewFragment;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

import static com.tokopedia.flight.cancellation.view.fragment.FlightCancellationReviewFragment.EXTRA_CANCEL_JOURNEY;
import static com.tokopedia.flight.cancellation.view.fragment.FlightCancellationReviewFragment.EXTRA_INVOICE_ID;

public class FlightCancellationReviewActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context, String invoiceId,
                                      FlightCancellationWrapperViewModel flightCancellationPassData) {
        Intent intent = new Intent(context, FlightCancellationReviewActivity.class);
        intent.putExtra(EXTRA_INVOICE_ID, invoiceId);
        intent.putExtra(EXTRA_CANCEL_JOURNEY, flightCancellationPassData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.grey_500));
        String title = getString(com.tokopedia.flight.R.string.activity_label_flight_review_cancellation);
        String subtitle = String.format(
                getString(com.tokopedia.flight.R.string.flight_cancellation_subtitle_order_id),
                getIntent().getExtras().getString(FlightCancellationFragment.EXTRA_INVOICE_ID)
        );
        updateTitle(title, subtitle);
    }

    @Override
    public FlightCancellationComponent getComponent() {
        return DaggerFlightCancellationComponent.builder()
                .flightComponent(getFlightComponent())
                .build();
    }

    @Override
    protected Fragment getNewFragment() {
        FlightCancellationWrapperViewModel flightCancellationJourneyList = getIntent().getExtras()
                .getParcelable(EXTRA_CANCEL_JOURNEY);
        return FlightCancellationReviewFragment.createInstance(
                getIntent().getExtras().getString(EXTRA_INVOICE_ID),
                flightCancellationJourneyList
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
