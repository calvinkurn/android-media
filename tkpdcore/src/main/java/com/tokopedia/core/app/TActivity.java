package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.localytics.android.Localytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.retrofit.utils.DialogForceLogout;
import com.tokopedia.core.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.service.HadesService;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.HockeyAppHelper;
import com.tokopedia.core.util.PhoneVerificationUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.var.ToolbarVariable;
import com.tokopedia.core.welcome.WelcomeActivity;

/**
 * Created by Nisie on 31/08/15.
 */
public abstract class TActivity extends BaseActivity {


    protected FrameLayout parentView;
    protected ToolbarVariable toolbar;
    private DrawerLayout drawerLayout;
    protected DrawerVariable drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Tokopedia3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }

        setContentView(R.layout.drawer_activity);
        parentView = (FrameLayout) findViewById(R.id.parent_view);
        toolbar = new ToolbarVariable(this);
        toolbar.createToolbarWithoutDrawer();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_nav);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (GlobalConfig.isSellerApp()) {
            drawer = ((TkpdCoreRouter)getApplication()).getDrawer(this);
        } else {
            drawer = new DrawerVariable(this);
        }

        drawer.setToolbar(toolbar);
        drawer.createDrawer();
        drawer.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawer.setHasUpdated(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!drawer.hasUpdated()) {
            drawer.updateData();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return onHomeOptionSelected();
        } else if (item.getItemId() == R.id.action_search) {
            return onSearchOptionSelected();
        } else if (item.getItemId() == R.id.action_cart) {
            return onCartOptionSelected();
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean onSearchOptionSelected() {
        Intent intent = BrowseProductRouter
                .getBrowseProductIntent(this, "0", TopAdsApi.SRC_BROWSE_PRODUCT);
        startActivity(intent);
        return true;
    }

    public static boolean onCartOptionSelected(Context context) {
        if (!SessionHandler.isV4Login(context)) {
            Intent intent = SessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            context.startActivity(intent);
        } else {
            context.startActivity(TransactionCartRouter.createInstanceCartActivity(context));
        }
        return true;
    }

    private Boolean onCartOptionSelected() {

        if (!SessionHandler.isV4Login(getBaseContext())) {
            Intent intent = SessionRouter.getLoginActivityIntent(getBaseContext());
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            startActivity(intent);
        } else {
            startActivity(TransactionCartRouter.createInstanceCartActivity(this));
        }
        return true;
    }

    private boolean onHomeOptionSelected() {
        KeyboardHandler.DropKeyboard(this, parentView);
        onBackPressed();
        return true;
    }

    public void inflateView(int layoutId) {
        getLayoutInflater().inflate(layoutId, parentView);
    }

    public void hideToolbar() {
        getSupportActionBar().hide();
    }
}
