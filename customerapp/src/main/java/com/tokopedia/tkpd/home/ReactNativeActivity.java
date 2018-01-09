package com.tokopedia.tkpd.home;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.ReactApplication;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.ReactNativeOfficialStoreFragment;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;

public class ReactNativeActivity extends BasePresenterActivity implements ReactNativeView {
    public static final String USER_ID = "User_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String PAGE_ID = "page_id";

    @DeepLink({Constants.Applinks.OFFICIAL_STORES})
    public static Intent getOfficialStoresApplinkCallingIntent(Context context, Bundle bundle) {
        return ReactNativeActivity.createOfficialStoresReactNativeActivity(
                context,
                ReactConst.Screen.OFFICIAL_STORE,
                context.getString(com.tokopedia.tkpd.R.string.react_native_banner_official_title),
                bundle
        );
    }

    @DeepLink({Constants.Applinks.DISCOVERY_PAGE})
    public static Intent getDiscoveryPaageIntent(Context context, Bundle bundle) {
        return ReactNativeActivity.createDiscoveryPageReactNativeActivity(
                context, ReactConst.Screen.DISCOVERY_PAGE,
                "",
                bundle.getString(PAGE_ID),
                bundle
        );
    }

    public static Intent createOfficialStoresReactNativeActivity(Context context,
                                                                 String reactScreenName,
                                                                 String pageTitle,
                                                                 Bundle extras) {
        Intent intent = new Intent(context, ReactNativeActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent createOfficialStoresReactNativeActivity(Context context,
                                                                 String reactScreenName,
                                                                 String pageTitle) {
        Intent intent = new Intent(context, ReactNativeActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent createDiscoveryPageReactNativeActivity(Context context,
                                                                String reactScreenName,
                                                                String pageTitle,
                                                                String pageId,
                                                                Bundle extras) {
        Intent intent = new Intent(context, ReactNativeActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        extras.putString(PAGE_ID, pageId);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent createDiscoveryPageReactNativeActivity(Context context,
                                                                String reactScreenName,
                                                                String pageTitle,
                                                                String pageId) {
        Intent intent = new Intent(context, ReactNativeActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        extras.putString(PAGE_ID, pageId);
        intent.putExtras(extras);
        return intent;
    }

    private void setToolbar() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            String title = getIntent().getExtras().getString(EXTRA_TITLE);
            if (!TextUtils.isEmpty(title)) {
                toolbar.setTitle(title);
            }
        }
    }


    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_react_native_official_stores;
    }

    @Override
    protected void initView() {
        setToolbar();
        Bundle initialProps = getReactNativeProps();
        ReactNativeOfficialStoreFragment fragment = ReactNativeOfficialStoreFragment.createInstance(initialProps);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    private Bundle getReactNativeProps() {
        Bundle bundle = getIntent().getExtras();
        Bundle newBundle = new Bundle();
        for (String key : bundle.keySet()) {
            if (!key.equalsIgnoreCase("is_deep_link_flag") &&
                    !key.equalsIgnoreCase("android.intent.extra.REFERRER") &&
                    !key.equalsIgnoreCase("deep_link_uri")){
                newBundle.putString(key, bundle.getString(key));
            }
        }
        return newBundle;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_OFFICIAL_STORE_REACT;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (GlobalConfig.isAllowDebuggingTools()
                && keyCode == KeyEvent.KEYCODE_MENU
                && ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager() != null) {
            ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void actionSetToolbarTitle(String title) {
        toolbar.setTitle(title);
    }
}
