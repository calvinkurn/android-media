package com.tokopedia.flight.airport.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;

/**
 * Created by nathan on 10/20/17.
 */

public class FlightAirportPickerActivity extends BaseSimpleActivity implements HasComponent<FlightComponent> {
    public static final String EXTRA_TOOLBAR_TITLE = "EXTRA_TOOLBAR_TITLE";

    public static Intent createInstance(Context context, String title) {
        Intent intent = new Intent(context, FlightAirportPickerActivity.class);
        intent.putExtra(EXTRA_TOOLBAR_TITLE, title);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightAirportPickerFragment.Companion.getInstance(
                getIntent().getExtras().getString(EXTRA_TOOLBAR_TITLE));
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    public FlightComponent getComponent() {
        return FlightComponentInstance.getFlightComponent(getApplication());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getIntent().getStringExtra(EXTRA_TOOLBAR_TITLE));
    }
}