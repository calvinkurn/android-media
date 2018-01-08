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
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.ReactNativeOfficialStorePromoFragment;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;


/**
 * Created by yogieputra on 05/01/18.
 */

public class ReactNativeOfficialStorePromoActivity extends BasePresenterActivity implements ReactNativeView {
    public static final String OS_PROMO_PAGE = "OS Promo Page";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URL = "EXTRA_URL";


    @DeepLink({Constants.Applinks.OFFICIAL_STORES_PROMO})
    public static Intent getOfficialStoresPromoApplinkCallingIntent(Context context, Bundle bundle){
        ScreenTracking.screen(OS_PROMO_PAGE);
        return ReactNativeOfficialStorePromoActivity.createBannerReactNativeActivity(
                context, ReactConst.Screen.PROMO,
                bundle.getString("slug")
        ).putExtras(bundle);
    }

    @DeepLink({Constants.Applinks.OFFICIAL_STORE_PROMO})
    public static Intent getOfficialStorePromoApplinkCallingIntent(Context context, Bundle bundle){
        ScreenTracking.screen(OS_PROMO_PAGE);
        return ReactNativeOfficialStorePromoActivity.createBannerReactNativeActivity(
                context, ReactConst.Screen.PROMO,
                bundle.getString("slug")
        ).putExtras(bundle);
    }

    @DeepLink({Constants.Applinks.OFFICIAL_STORES_PROMO_TERMS})
    public static Intent getOfficialStoreTermsIntent(Context context, Bundle bundle){
        return ReactNativeOfficialStorePromoActivity.createOfficialStoreTerms(
                context,
                ReactConst.Screen.PROMO,
                "Syarat & Ketentuan",
                bundle
        );
    }




    public static Intent createBannerReactNativeActivity(Context context, String reactScreenName, String url){
        Intent intent = new Intent(context, ReactNativeOfficialStorePromoActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, "");
        extras.putString(EXTRA_URL, url);
        intent.putExtras(extras);
        return intent;
    }

    private static Intent createOfficialStoreTerms(Context context, String reactScreenName, String title, Bundle extras){
        Intent intent = new Intent(context, ReactNativeOfficialStorePromoActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(ReactConst.SUB_PAGE, ReactConst.Screen.PROMO_TERMS);
        extras.putString(EXTRA_TITLE, title);
        intent.putExtras(extras);
        return intent;
    }

    private void setToolbar(){
        if (getIntent() != null && getIntent().getExtras() != null){
            String title = getIntent().getExtras().getString(EXTRA_TITLE);
            if (!TextUtils.isEmpty(title)){
                toolbar.setTitle(title);
            }
        }
    }

    private Bundle getReactNativeProps(){
        Bundle bundle = getIntent().getExtras();
        Bundle newBundle = new Bundle();
        for (String key : bundle.keySet()){
            if (!key.equalsIgnoreCase("is_deep_link_flag") &&
                    !key.equalsIgnoreCase("android.intent.extra.REFERRER") &&
                    !key.equalsIgnoreCase("deep_link_uri")){
                newBundle.putString(key, bundle.getString(key));
            }
        }
        return newBundle;
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if (GlobalConfig.isAllowDebuggingTools()
                && keyCode == KeyEvent.KEYCODE_MENU
                && ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager() != null){
            ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void actionSetToolbarTitle(String title) {
        toolbar.setTitle(title);
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
        ReactNativeOfficialStorePromoFragment fragment = ReactNativeOfficialStorePromoFragment.createInstance(initialProps);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
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
}

