package com.tokopedia.flight.common.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.orderlist.view.FlightOrderListActivity;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import static com.tokopedia.flight.common.constant.FlightUrl.CONTACT_US_FLIGHT;
import static com.tokopedia.flight.common.constant.FlightUrl.FLIGHT_PROMO_APPLINK;

/**
 * Created by alvarisi on 12/5/17.
 */

public abstract class BaseFlightActivity extends BaseSimpleActivity {


    @Inject
    public FlightAnalytics flightAnalytics;
    @Inject
    public UserSessionInterface userSession;

    private FlightComponent component;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        initInjector();
    }

    private void initInjector() {
        getFlightComponent().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(com.tokopedia.flight.R.menu.menu_flight_dashboard, menu);
        updateOptionMenuColorWhite(menu);
        return true;
    }

    protected FlightComponent getFlightComponent() {
        if (component == null) {
            component = FlightComponentInstance.getFlightComponent(getApplication());
        }
        return component;
    }

    protected void navigateToAllPromoPage() {
        RouteManager.route(this, FLIGHT_PROMO_APPLINK);
    }

    protected void navigateToHelpPage() {
        RouteManager.route(this, CONTACT_US_FLIGHT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.tokopedia.flight.R.id.menu_promo) {
            navigateToAllPromoPage();
            return true;
        } else if (item.getItemId() == com.tokopedia.flight.R.id.menu_transaction_list) {
            if (userSession.isLoggedIn()) {
                flightAnalytics.eventClickTransactions(getScreenName());
                startActivity(FlightOrderListActivity.getCallingIntent(this));
            } else {
                RouteManager.route(this, ApplinkConst.LOGIN);
            }
            return true;
        } else if (item.getItemId() == com.tokopedia.flight.R.id.menu_help) {
            navigateToHelpPage();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
