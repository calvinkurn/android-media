package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.booking.view.fragment.FlightInsuranceWebViewFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;

public class FlightInsuranceWebviewActivity extends BaseSimpleActivity implements HasComponent<FlightComponent> {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static Intent getCallingIntent(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, FlightInsuranceWebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getIntent().getStringExtra(EXTRA_TITLE));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(
                    ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default)
            );
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightInsuranceWebViewFragment.newInstance(getIntent().getStringExtra(EXTRA_URL));
    }

    @Override
    public FlightComponent getComponent() {
        return FlightComponentInstance.getFlightComponent(getApplication());
    }
}
