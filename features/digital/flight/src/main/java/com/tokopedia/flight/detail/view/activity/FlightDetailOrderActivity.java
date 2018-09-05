package com.tokopedia.flight.detail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.applink.ApplinkConstant;
import com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderActivity extends BaseSimpleActivity implements HasComponent<FlightOrderComponent> {

    public static final String EXTRA_ORDER_PASS_DETAIL = "EXTRA_ORDER_PASS_DETAIL";

    public static Intent createIntent(Context context, FlightOrderDetailPassData flightOrderDetailPassData) {
        Intent intent = new Intent(context, FlightDetailOrderActivity.class);
        intent.putExtra(EXTRA_ORDER_PASS_DETAIL, flightOrderDetailPassData);
        return intent;
    }

    @DeepLink(ApplinkConstant.FLIGHT_ORDER_DETAIL)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        FlightOrderDetailPassData passData = new FlightOrderDetailPassData();
        passData.setOrderId(extras.getString("id"));

        if (context.getApplicationContext() instanceof AbstractionRouter){
            UserSession userSession = ((AbstractionRouter) context.getApplicationContext()).getSession();

            if (!userSession.isLoggedIn()) {
                return ((FlightModuleRouter) context.getApplicationContext()).getLoginIntent();
            } else {
                Intent intent = new Intent(context, FlightDetailOrderActivity.class);
                intent.putExtra(EXTRA_ORDER_PASS_DETAIL, passData);
                return intent
                        .setData(uri.build());
            }
        }

        return ((FlightModuleRouter) context.getApplicationContext())
                .getHomeIntent(context.getApplicationContext());
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightDetailOrderFragment.createInstance((FlightOrderDetailPassData) getIntent().getParcelableExtra(EXTRA_ORDER_PASS_DETAIL));
    }

    @Override
    public FlightOrderComponent getComponent() {
        return DaggerFlightOrderComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

}
