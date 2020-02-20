package com.tokopedia.flight.detail.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import static com.tokopedia.flight.orderlist.view.FlightOrderListActivity.EXTRA_IS_CANCELLATION;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderActivity extends BaseFlightActivity implements HasComponent<FlightComponent> {

    private static final int REQUEST_CODE_LOGIN = 6;

    @Inject
    UserSessionInterface userSession;

    FlightOrderDetailPassData passData;
    String isCancellation;

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
    public FlightComponent getComponent() {
        return FlightComponentInstance.getFlightComponent(getApplication());
    }

}
