package com.tokopedia.flight.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.applink.ApplinkConstant;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.FlightDashboardFragment;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardActivity extends BaseFlightActivity implements HasComponent<FlightDashboardComponent> {

    private static final String EXTRA_TRIP = "EXTRA_TRIP";
    private static final String EXTRA_ADULT = "EXTRA_ADULT";
    private static final String EXTRA_CHILD = "EXTRA_CHILD";
    private static final String EXTRA_INFANT = "EXTRA_INFANT";
    private static final String EXTRA_CLASS = "EXTRA_CLASS";
    private static final String EXTRA_AUTO_SEARCH = "EXTRA_AUTO_SEARCH";

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, FlightDashboardActivity.class);
    }

    @DeepLink(ApplinkConstant.FLIGHT)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        return intent
                .setData(uri.build())
                .putExtras(extras);
    }

    @DeepLink(ApplinkConstant.FLIGHT_SEARCH)
    public static Intent getCallingApplinkSearchIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        intent.putExtra(EXTRA_TRIP, extras.getString("dest"));
        intent.putExtra(EXTRA_ADULT, extras.getString("a"));
        intent.putExtra(EXTRA_CHILD, extras.getString("c"));
        intent.putExtra(EXTRA_INFANT, extras.getString("i"));
        intent.putExtra(EXTRA_CLASS, extras.getString("s"));
        intent.putExtra(EXTRA_AUTO_SEARCH, extras.getString("auto_search"));

        return intent
                .setData(uri.build());
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent().hasExtra(EXTRA_TRIP) && getIntent().hasExtra(EXTRA_ADULT) && getIntent().hasExtra(EXTRA_CHILD)
                && getIntent().hasExtra(EXTRA_INFANT) && getIntent().hasExtra(EXTRA_CLASS) && getIntent().hasExtra(EXTRA_AUTO_SEARCH)) {
            return FlightDashboardFragment.getInstance(
                    getIntent().getStringExtra(EXTRA_TRIP),
                    getIntent().getStringExtra(EXTRA_ADULT),
                    getIntent().getStringExtra(EXTRA_CHILD),
                    getIntent().getStringExtra(EXTRA_INFANT),
                    getIntent().getStringExtra(EXTRA_CLASS),
                    getIntent().getStringExtra(EXTRA_AUTO_SEARCH)
            );
        } else {
            return FlightDashboardFragment.getInstance();
        }
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
    public String getScreenName() {
        return FlightAnalytics.Screen.HOMEPAGE;
    }
}