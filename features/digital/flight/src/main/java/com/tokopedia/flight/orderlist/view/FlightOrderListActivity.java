package com.tokopedia.flight.orderlist.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.applink.ApplinkConstant;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;

import static com.tokopedia.flight.orderlist.view.FlightOrderListFragment.DEFAULT_CHECK_PRELOAD;
import static com.tokopedia.flight.orderlist.view.FlightOrderListFragment.EXTRA_SHOULD_CHECK_PRELOAD;

/**
 * Created by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListActivity extends BaseSimpleActivity implements HasComponent<FlightOrderComponent> {
    FlightOrderComponent component;


    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightOrderListActivity.class);
    }

    public static Intent getCallingIntent(Activity activity, boolean isShouldCheckPreload) {
        return new Intent(activity, FlightOrderListActivity.class)
                .putExtra(EXTRA_SHOULD_CHECK_PRELOAD, isShouldCheckPreload);
    }

    @DeepLink(ApplinkConstant.FLIGHT_ORDER)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, FlightOrderListActivity.class);
        return intent
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightOrderListFragment.createInstance(
                getIntent().getBooleanExtra(EXTRA_SHOULD_CHECK_PRELOAD, DEFAULT_CHECK_PRELOAD));
    }

    @Override
    public FlightOrderComponent getComponent() {
        if (component == null) {
            component = DaggerFlightOrderComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                    .build();
        }
        return component;
    }
}