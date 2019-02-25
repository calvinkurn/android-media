package com.tokopedia.developer_options.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.developer_options.R;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeDeveloperOptionsActivity
        extends ReactFragmentActivity<GeneralReactNativeFragment> {
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    @DeepLink({ApplinkConst.SETTING_DEVELOPER_OPTIONS})
    public static Intent reactDevOptDeepLinkIntent(Context ctx, Bundle extras) {
        return createScreenLink(
                ctx,
                ReactConst.Screen.DEV_OPTIONS,
                ctx.getString(R.string.react_native_logger_title),
                extras);
    }

    private static Intent createScreenLink(Context ctx,
                                           String screenName,
                                           String pageTitle,
                                           Bundle extras) {
        Intent intent = new Intent(ctx, ReactNativeDeveloperOptionsActivity.class);

        extras.putString(ReactConst.KEY_SCREEN, screenName);
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
        Intent intent = getIntentReactFragment();

        if (intent != null && intent.getExtras() != null) {
            return intent.getExtras().getString(EXTRA_TITLE);
        }

        return "";
    }
}
