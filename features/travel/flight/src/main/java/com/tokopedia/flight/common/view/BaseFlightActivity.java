package com.tokopedia.flight.common.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.common.travel.widget.TravelMenuBottomSheet;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.constant_kotlin.FlightUrl;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/5/17.
 */

public abstract class BaseFlightActivity extends BaseSimpleActivity implements TravelMenuBottomSheet.TravelMenuListener {


    @Inject
    public FlightAnalytics flightAnalytics;
    @Inject
    public UserSessionInterface userSession;

    private FlightComponent component;

    static String TAG_FLIGHT_MENU = "flightMenu";

    static int REQUEST_CODE_LOGIN_FLIGHT = 101;

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_overflow_menu) {
            showBottomMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected FlightComponent getFlightComponent() {
        if (component == null) {
            component = FlightComponentInstance.getFlightComponent(getApplication());
        }
        return component;
    }

    protected void navigateToAllPromoPage() {
        RouteManager.route(this, FlightUrl.FLIGHT_PROMO_APPLINK);
    }

    protected void navigateToHelpPage() {
        RouteManager.route(this, FlightUrl.INSTANCE.getCONTACT_US_FLIGHT());
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        showBottomMenu();
        return false;
    }

    public void showBottomMenu() {
        TravelMenuBottomSheet flightMenuBottomSheet = new TravelMenuBottomSheet();
        flightMenuBottomSheet.listener = this;
        flightMenuBottomSheet.show(getSupportFragmentManager(), TAG_FLIGHT_MENU);
    }

    @Override
    public void onHelpClicked() {
        navigateToHelpPage();
    }

    @Override
    public void onOrderListClicked() {
        flightAnalytics.eventClickTransactions(getScreenName());
        RouteManager.route(this, ApplinkConst.FLIGHT_ORDER);
    }

    @Override
    public void onPromoClicked() {
        navigateToAllPromoPage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOGIN_FLIGHT) {
            RouteManager.route(this, ApplinkConst.FLIGHT_ORDER);
        }
    }
}
