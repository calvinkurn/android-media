package com.tokopedia.gm.subscribe.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.presentation.BaseTemporaryDrawerActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.gm.common.constant.GMParamConstant;
import com.tokopedia.gm.subscribe.R;
import com.tokopedia.gm.subscribe.view.fragment.GmHomeFragment;
import com.tokopedia.gm.subscribe.view.fragment.GmHomeFragmentCallback;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GmSubscribeHomeActivity
        extends BaseTemporaryDrawerActivity
        implements GmHomeFragmentCallback,
        HasComponent<AppComponent> {


    public static final int REQUEST_PRODUCT = 1;
    private FragmentManager fragmentManager;
    public boolean isFromFeatured = false;

    @DeepLink(Constants.Applinks.SellerApp.GOLD_MERCHANT)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return getCallingIntent(context)
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, GmSubscribeHomeActivity.class);
    }

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gm_subscribe;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        fragmentManager = getFragmentManager();
        isFromFeatured = getIntent().getBooleanExtra(GMParamConstant.PARAM_KEY_FROM_FEATURE, false);
    }

    @Override
    protected void setActionVar() {
        goToGMHome();
    }

    public void goToGMHome() {
        if (fragmentManager.findFragmentByTag(GmHomeFragment.TAG) == null) {
            inflateFragment(
                    GmHomeFragment.createFragment(isFromFeatured),
                    false,
                    GmHomeFragment.TAG);
        }
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.parent_view, fragment, tag);
        if (isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void goToGMProductFristTime() {
        Intent intent = GmProductActivity.selectProductFirstTime(this);
        startActivityForResult(intent, REQUEST_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PRODUCT) {
            if (resultCode == Activity.RESULT_OK) {
                processIntent(data);
            }
        }
    }

    private void processIntent(Intent data) {
        Bundle bundle = data.getExtras();
        int selected = bundle.getInt(GmProductActivity.SELECTED_PRODUCT);
        Intent intent = GmCheckoutActivity.processSelectedProduct(this, selected);
        startActivity(intent);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GM_SUBSCRIBE;
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent = null;
            if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(this);
            } else {
                homeIntent = HomeRouter.getHomeActivity(this);
            }
            startActivity(homeIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
