package com.tokopedia.tkpd.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

import java.util.Set;

/**
 * Created by okasurya on 1/9/18.
 */

public class ReactNativeDiscoveryActivity extends ReactFragmentActivity<GeneralReactNativeFragment> implements PermissionAwareActivity {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String PAGE_ID = "page_id";
    public static final String SHAKE_SHAKE = "shake-shake";
    private static final String TAG = "ReactNativeDiscoveryActivity";
    private static final String MP_FLASHSALE = "mp_flashsale";
    private static boolean mAllowShake = true;
    private PermissionListener mPermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCrashLog();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @DeepLink({ApplinkConst.REACT_DISCOVERY_PAGE})
    public static Intent getDiscoveryPageIntent(Context context, Bundle bundle) {
        if (bundle != null) {
            String key = getKeyValueByCaseInsensitive(bundle);
            if (key != null && !key.isEmpty()) {
                mAllowShake = Boolean.parseBoolean(key);
            }
        }
        ReactUtils.startTracing(MP_FLASHSALE);
        return ReactNativeDiscoveryActivity.createApplinkCallingIntent(
                context, ReactConst.Screen.DISCOVERY_PAGE,
                "",
                bundle.getString(PAGE_ID),
                bundle
        );
    }

    private static String getKeyValueByCaseInsensitive(Bundle bundle) {
        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            if (key.toLowerCase().equals(SHAKE_SHAKE))
                return bundle.getString(key);
        }
        return null;
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
        ReactUtils.startTracing(MP_FLASHSALE);
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

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Do not put super, avoid crash transactionTooLarge
    }

    // Setting custom logs for Discovery Activity with Page ID
    public void setCrashLog() {
        if (!GlobalConfig.DEBUG && getIntent() != null && getIntent().hasExtra(PAGE_ID)) {
            FirebaseCrashlytics.getInstance().log(TAG + " " + getIntent().getExtras().getString(PAGE_ID));
        }

    }
}