package com.tokopedia.tkpd.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

/**
 * Created by okasurya on 1/9/18.
 */

public class ReactNativeDiscoveryActivity extends ReactFragmentActivity<GeneralReactNativeFragment> implements PermissionAwareActivity {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String PAGE_ID = "page_id";
    private PermissionListener mPermissionListener;

    @DeepLink({Constants.Applinks.DISCOVERY_PAGE})
    public static Intent getDiscoveryPageIntent(Context context, Bundle bundle) {
        return ReactNativeDiscoveryActivity.createApplinkCallingIntent(
                context, ReactConst.Screen.DISCOVERY_PAGE,
                "",
                bundle.getString(PAGE_ID),
                bundle
        );
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

    private static Intent createApplinkCallingIntent(Context context,
                                                     String reactScreenName,
                                                     String pageTitle,
                                                     String pageId,
                                                     Bundle extras) {
        Intent intent = new Intent(context, ReactNativeDiscoveryActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        extras.putString(PAGE_ID, pageId);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent createCallingIntent(Context context,
                                             String reactScreenName,
                                             String pageTitle,
                                             String pageId) {
        Intent intent = new Intent(context, ReactNativeDiscoveryActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        extras.putString(PAGE_ID, pageId);
        intent.putExtras(extras);
        return intent;
    }
    // For allowing native permission in react native
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mPermissionListener = listener;
        requestPermissions(permissions, requestCode);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mPermissionListener != null && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            mPermissionListener = null;
        }
    }
}
