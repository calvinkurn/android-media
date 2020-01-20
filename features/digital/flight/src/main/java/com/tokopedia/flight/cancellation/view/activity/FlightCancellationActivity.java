package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.Menu;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationFragment;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.flight.cancellation.view.fragment.FlightCancellationFragment.EXTRA_CANCEL_JOURNEY;
import static com.tokopedia.flight.cancellation.view.fragment.FlightCancellationFragment.EXTRA_INVOICE_ID;

public class FlightCancellationActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context,
                                      String invoiceId,
                                      List<FlightCancellationJourney> flightCancellationJourneyList) {
        Intent intent = new Intent(context, FlightCancellationActivity.class);
        intent.putExtra(EXTRA_INVOICE_ID, invoiceId);
        intent.putParcelableArrayListExtra(EXTRA_CANCEL_JOURNEY, (ArrayList<? extends Parcelable>) flightCancellationJourneyList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        List<FlightCancellationJourney> flightCancellationJourneyList = getIntent().getExtras().getParcelableArrayList(EXTRA_CANCEL_JOURNEY);
        return FlightCancellationFragment.createInstance(
                getIntent().getExtras().getString(EXTRA_INVOICE_ID),
                flightCancellationJourneyList
        );
    }

    private void setupToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.grey_500));
        String title = getString(com.tokopedia.flight.R.string.activity_label_flight_cancellation);
        String subtitle = String.format(
                getString(com.tokopedia.flight.R.string.flight_cancellation_subtitle_order_id),
                getIntent().getExtras().getString(EXTRA_INVOICE_ID)
        );
        updateTitle(title, subtitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
