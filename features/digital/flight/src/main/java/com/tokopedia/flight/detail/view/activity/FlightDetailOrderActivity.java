package com.tokopedia.flight.detail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderActivity extends BaseSimpleActivity implements HasComponent<FlightOrderComponent> {

    private static final int REQUEST_CODE_LOGIN = 6;
    public static final String EXTRA_ORDER_PASS_DETAIL = "EXTRA_ORDER_PASS_DETAIL";
    public static final String EXTRA_IS_CANCELLATION = "is_cancellation";

    @Inject
    UserSessionInterface userSession;

    FlightOrderDetailPassData passData;
    String isCancellation;

    public static Intent createIntent(Context context, FlightOrderDetailPassData flightOrderDetailPassData) {
        Intent intent = new Intent(context, FlightDetailOrderActivity.class);
        intent.putExtra(EXTRA_ORDER_PASS_DETAIL, flightOrderDetailPassData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        if (userSession.isLoggedIn()) {
            passData = new FlightOrderDetailPassData();
            Uri uri = getIntent().getData();
            if (uri != null) {
                passData.setOrderId(uri.getLastPathSegment());
                if (uri.getQueryParameter(EXTRA_IS_CANCELLATION) != null && uri.getQueryParameter(EXTRA_IS_CANCELLATION).length() > 0) {
                    isCancellation = uri.getQueryParameter(EXTRA_IS_CANCELLATION);
                }
            }
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOGIN) {
            if (userSession.isLoggedIn()) recreate();
            else finish();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightDetailOrderFragment.createInstance(
                passData,
                isCancellation != null && isCancellation.equals("1"));
    }

    @Override
    public FlightOrderComponent getComponent() {
        return DaggerFlightOrderComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

}
