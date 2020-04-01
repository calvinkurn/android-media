package com.tokopedia.flight.orderlist.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.view.fragment.FlightOrderListFragment;

/**
 * Created by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListActivity extends BaseSimpleActivity implements HasComponent<FlightOrderComponent> {
    FlightOrderComponent component;

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";
    public static final String EXTRA_IS_CANCELLATION = "is_cancellation";
    public static final String EXTRA_IS_AFTER_CANCELLATION = "EXTRA_IS_AFTER_CANCELLATION";

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightOrderListActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightOrderListFragment.createInstance();
    }

    @Override
    public FlightOrderComponent getComponent() {
        if (component == null) {
            component = DaggerFlightOrderComponent.builder().baseAppComponent(
                    ((BaseMainApplication) getApplication()).getBaseAppComponent())
                    .build();
        }
        return component;
    }
}