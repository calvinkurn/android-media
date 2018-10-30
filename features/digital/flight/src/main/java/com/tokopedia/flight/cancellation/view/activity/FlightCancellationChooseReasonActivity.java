package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.common.view.BaseFlightActivity;

public class FlightCancellationChooseReasonActivity extends BaseFlightActivity
        implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, FlightCancellationChooseReasonActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public FlightCancellationComponent getComponent() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
