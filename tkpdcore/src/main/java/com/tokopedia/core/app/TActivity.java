package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.var.ToolbarVariable;

/**
 * Created by Nisie on 31/08/15.
 */
public abstract class TActivity extends BaseActivity {

    protected FrameLayout parentView;
    protected ToolbarVariable toolbarVariable;
    protected Toolbar toolbar;

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
        setupToolbar();
    }

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

}
