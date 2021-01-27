package com.tokopedia.sellerapp.deeplink;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.config.GlobalConfig;

import com.tokopedia.sellerapp.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by Nisie on 31/08/15.
 */

/**
 * Extends one of BaseActivity from tkpd abstraction eg:BaseSimpleActivity, BaseStepperActivity, BaseTabActivity, etc
 */
@Deprecated
public abstract class TActivity extends BaseActivity {

    private final static String CART_ACTIVITY_NEW
            = "com.tokopedia.purchase_platform.features.cart.view.CartActivity";

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

    private Boolean onCartOptionSelected() {
        UserSessionInterface userSession = new UserSession(this);
        if (!userSession.isLoggedIn()) {
            Intent intent = RouteManager.getIntent(this, ApplinkConst.LOGIN);
            startActivity(intent);
        } else {
            startActivity(getActivityIntent(this, CART_ACTIVITY_NEW));
        }
        return true;
    }

    private static Intent getActivityIntent(Context context, String activityFullPath) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), activityFullPath);
        return intent;
    }

    public boolean onHomeOptionSelected() {
        if (parentView != null) KeyboardHandler.DropKeyboard(this, parentView);
        onBackPressed();
        return true;
    }

    public void inflateView(int layoutId) {
        if (parentView != null) getLayoutInflater().inflate(layoutId, parentView);
    }

    protected void setLightToolbarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
            toolbar.setBackgroundResource(R.color.white);
        } else {
            toolbar.setBackgroundResource(com.tokopedia.resources.common.R.drawable.bg_white_toolbar_drop_shadow);
        }
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_toolbar_overflow_level_two_black);
        if (drawable != null)
            drawable.setBounds(5, 5, 5, 5);

        toolbar.setOverflowIcon(drawable);

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(
                    R.drawable.ic_webview_back_button
            );

        toolbar.setTitleTextAppearance(this, R.style.WebViewToolbarText);
        toolbar.setSubtitleTextAppearance(this, R.style
                .WebViewToolbarSubtitleText);
    }

    // for global nav purpose
    protected boolean isLightToolbarThemes() {
        return !GlobalConfig.isSellerApp();
    }
}
