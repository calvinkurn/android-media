package com.tokopedia.pms.howtopay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;

/**
 * Created by yogieputra on 10/07/18.
 */

public class ReactNativeHowToPayActivity extends BaseSimpleActivity {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String IS_DEEP_LINK_FLAG = "is_deep_link_flag";
    public static final String ANDROID_INTENT_EXTRA_REFERRER = "android.intent.extra.REFERRER";
    public static final String DEEP_LINK_URI = "deep_link_uri";

    private static final String MP_PAYMENT_MANAGEMENT_SYSTEM = "mp_payment_management_system";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReactUtils.startTracing(MP_PAYMENT_MANAGEMENT_SYSTEM);
    }

    @Override
    protected Fragment getNewFragment() {
        return ReactNativeHowToPayFragment.createInstance(getReactNativeProps());
    }

    protected Bundle getReactNativeProps() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.HOW_TO_PAY);
        bundle.putString(EXTRA_TITLE, "Cara Pembayaran");
        Bundle newBundle = new Bundle();

        // clear bundle from deeplinks default value
        for (String key : bundle.keySet()) {
            if (!key.equalsIgnoreCase(IS_DEEP_LINK_FLAG) &&
                    !key.equalsIgnoreCase(ANDROID_INTENT_EXTRA_REFERRER) &&
                    !key.equalsIgnoreCase(DEEP_LINK_URI)) {
                newBundle.putString(key, bundle.getString(key));
            }
        }
        return newBundle;
    }
}
