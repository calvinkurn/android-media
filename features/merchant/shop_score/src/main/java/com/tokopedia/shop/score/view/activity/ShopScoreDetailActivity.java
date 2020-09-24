package com.tokopedia.shop.score.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.shop.score.R;
import com.tokopedia.shop.score.view.fragment.ShopScoreDetailFragment;
import com.tokopedia.shop.score.view.fragment.ShopScoreDetailFragmentCallback;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailActivity extends BaseSimpleActivity implements ShopScoreDetailFragmentCallback {
    private static final String SELLER_CENTER_LINK = "https://seller.tokopedia.com/edu/skor-toko";
    private static final String SHOP_SCORE_INFORMATION = "https://help.tokopedia.com/hc/en-us/articles/115000854466-Performa-Toko";

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_score_detail;
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SELLER_SHOP_SCORE;
    }

    @Override
    public void goToSellerCenter() {
        openUrlWebView(SELLER_CENTER_LINK);
    }

    @Override
    public void goToCompleteInformation() {
        openUrl(Uri.parse(SHOP_SCORE_INFORMATION));
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = (getSupportFragmentManager().findFragmentByTag(ShopScoreDetailFragment.TAG));
        if (fragment == null) {
            fragment = ShopScoreDetailFragment.createFragment();
        }
        inflateFragment(fragment, false, ShopScoreDetailFragment.TAG);
        return null;
    }

    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.tkpdabstraction_green_600));
        }
        getWindow().getDecorView().setSystemUiVisibility(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.shop_score_title));
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    private void openUrl(Uri parse) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, parse);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            NetworkErrorHelper.showSnackbar(this, getString(R.string.error_no_browser));
        }
    }

    private void openUrlWebView(String urlString) {
        String webViewApplink = ApplinkConst.WEBVIEW + "?url=" + urlString;
        RouteManager.route(this, webViewApplink);
    }
}
