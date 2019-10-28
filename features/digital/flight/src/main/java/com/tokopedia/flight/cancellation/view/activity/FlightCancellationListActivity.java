package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Menu;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationListFragment;
import com.tokopedia.flight.common.view.BaseFlightActivity;

import static com.tokopedia.flight.cancellation.view.fragment.FlightCancellationListFragment.EXTRA_INVOICE_ID;

public class FlightCancellationListActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context, String invoiceId) {
        Intent intent = new Intent(context, FlightCancellationListActivity.class);
        intent.putExtra(EXTRA_INVOICE_ID, invoiceId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public FlightCancellationComponent getComponent() {
        return DaggerFlightCancellationComponent.builder()
                .flightComponent(getFlightComponent())
                .build();
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightCancellationListFragment.createInstance(
                getIntent().getExtras().getString(EXTRA_INVOICE_ID)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
