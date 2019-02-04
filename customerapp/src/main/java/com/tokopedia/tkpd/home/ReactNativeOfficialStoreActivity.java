package com.tokopedia.tkpd.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeOfficialStoreActivity extends ReactFragmentActivity<GeneralReactNativeFragment> {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    private static final String MP_OFFICIAL_STORE = "mp_official_store";
    private static final String CATEGORY = "Category";
    private static final String KEY_CATEGORY = "key_category";

    @DeepLink({ApplinkConst.OFFICIAL_STORES, ApplinkConst.OFFICIAL_STORES_CATEGORY})
    public static Intent getOfficialStoresApplinkCallingIntent(Context context, Bundle bundle) {
        ReactUtils.startTracing(MP_OFFICIAL_STORE);
        return ReactNativeOfficialStoreActivity.createApplinkCallingIntent(
                context,
                ReactConst.Screen.OFFICIAL_STORE,
                context.getString(com.tokopedia.tkpd.R.string.react_native_banner_official_title),
                bundle
        );
    }

    public static Intent createApplinkCallingIntent(Context context,
                                                    String reactScreenName,
                                                    String pageTitle,
                                                    Bundle extras) {
        Intent intent = new Intent(context, ReactNativeOfficialStoreActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);

        if (extras.getString(KEY_CATEGORY) != null &&
                !extras.getString(KEY_CATEGORY).isEmpty()){
            extras.putString(CATEGORY, extras.getString(KEY_CATEGORY));
        }

        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent createCallingIntent(Context context,
                                             String reactScreenName,
                                             String pageTitle) {
        Intent intent = new Intent(context, ReactNativeOfficialStoreActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected GeneralReactNativeFragment getReactNativeFragment() {
        return GeneralReactNativeFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }

        return "";
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // No super to avoid crash transactionTooLarge
    }
}
