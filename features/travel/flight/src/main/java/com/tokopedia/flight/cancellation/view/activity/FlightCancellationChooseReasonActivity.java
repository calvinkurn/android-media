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
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationChooseReasonFragment;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

public class FlightCancellationChooseReasonActivity extends BaseFlightActivity
        implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context,
                                      FlightCancellationReasonViewModel selectedReason) {
        Intent intent = new Intent(context, FlightCancellationChooseReasonActivity.class);
        intent.putExtra(FlightCancellationChooseReasonFragment.EXTRA_SELECTED_REASON, selectedReason);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar();
    }

    @Override
    protected Fragment getNewFragment() {
        FlightCancellationReasonViewModel selectedReason = getIntent()
                .getParcelableExtra(FlightCancellationChooseReasonFragment.EXTRA_SELECTED_REASON);
        return FlightCancellationChooseReasonFragment.createInstance(selectedReason);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(com.tokopedia.common.travel.R.anim.travel_anim_stay, com.tokopedia.common.travel.R.anim.travel_slide_out_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void setupToolbar() {
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat
                .getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
    }

    @Override
    public FlightCancellationComponent getComponent() {
        return DaggerFlightCancellationComponent.builder()
                .flightComponent(getFlightComponent())
                .build();
    }
}
