package com.tokopedia.tkpd.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeOfficialStoreActivity extends ReactFragmentActivity<GeneralReactNativeFragment> {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    @DeepLink({Constants.Applinks.OFFICIAL_STORES})
    public static Intent getOfficialStoresApplinkCallingIntent(Context context, Bundle bundle) {
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
}
