package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.core2.R;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by Nisie on 31/08/15.
 */

/**
 * Extends one of BaseActivity from tkpd abstraction eg:BaseSimpleActivity, BaseStepperActivity, BaseTabActivity, etc
 */
@Deprecated
public abstract class TActivity extends BaseActivity {

    protected FrameLayout parentView;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Tokopedia3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }

        setContentView(getContentId());

        parentView = (FrameLayout) findViewById(R.id.parent_view);
        setupToolbar();
    }

    protected int getContentId() {
        return R.layout.main_activity;
    }

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (isLightToolbarThemes()) {
            setLightToolbarStyle();
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
        Intent intent = RouteManager.getIntent(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE);
        startActivity(intent);
        return true;
    }

    public static boolean onCartOptionSelected(Context context) {
        if (!SessionHandler.isV4Login(context)) {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getLoginIntent
                    (context);
            context.startActivity(intent);
        } else {
            context.startActivity(TransactionCartRouter.createInstanceCartActivity(context));
        }
        return true;
    }

    private Boolean onCartOptionSelected() {

        if (!SessionHandler.isV4Login(getBaseContext())) {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getLoginIntent
                    (this);
            startActivity(intent);
        } else {
            startActivity(TransactionCartRouter.createInstanceCartActivity(this));
        }
        return true;
    }

    public boolean onHomeOptionSelected() {
        if (parentView != null) KeyboardHandler.DropKeyboard(this, parentView);
        onBackPressed();
        return true;
    }

    public void inflateView(int layoutId) {
        if (parentView != null) getLayoutInflater().inflate(layoutId, parentView);
    }

    public void hideToolbar() {
        getSupportActionBar().hide();
    }

    protected void setLightToolbarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
            toolbar.setBackgroundResource(R.color.white);
        } else {
            toolbar.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);
        }
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_toolbar_overflow_level_two_black);
        if (drawable != null)
            drawable.setBounds(5, 5, 5, 5);

        toolbar.setOverflowIcon(drawable);

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(
                    com.tokopedia.core2.R.drawable.ic_webview_back_button
            );

        toolbar.setTitleTextAppearance(this, com.tokopedia.core2.R.style.WebViewToolbarText);
        toolbar.setSubtitleTextAppearance(this, com.tokopedia.core2.R.style
                .WebViewToolbarSubtitleText);
    }

    // for global nav purpose
    protected boolean isLightToolbarThemes() {
        return !GlobalConfig.isSellerApp();
    }
}
