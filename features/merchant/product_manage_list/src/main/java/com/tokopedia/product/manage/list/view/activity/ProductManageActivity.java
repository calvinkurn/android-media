package com.tokopedia.product.manage.list.view.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.product.manage.list.constant.AppScreen;
import com.tokopedia.product.manage.list.view.fragment.ProductManageSellerFragment;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * Created by zulfikarrahman on 9/25/17.
 */
public class ProductManageActivity extends BaseSimpleActivity {

    public UserSessionInterface userSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(this);
        setupLayout(savedInstanceState);
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return new ProductManageSellerFragment();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        SplitCompat.installActivity(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLogin();
    }

    @Override
    public void onBackPressed() {
        goToSellerAppDashboard();
        super.onBackPressed();
    }

    private void checkLogin() {
        if (!userSession.isLoggedIn()) {
            RouteManager.route(this, ApplinkConst.LOGIN);
            finish();
        } else if (!userSession.hasShop()) {
            RouteManager.route(this, ApplinkConst.HOME);
            finish();
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_MANAGE_PROD;
    }

    private void goToSellerAppDashboard() {
        if(GlobalConfig.isSellerApp()) {
            RouteManager.route(this, ApplinkConstInternalMarketplace.SELLER_APP_DASHBOARD);
        }
    }
}
