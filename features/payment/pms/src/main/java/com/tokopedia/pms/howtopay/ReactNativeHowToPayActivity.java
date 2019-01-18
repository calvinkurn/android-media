package com.tokopedia.pms.howtopay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
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

    @DeepLink(ApplinkConst.HOWTOPAY)
    public static Intent getHowToPayApplinkCallingIntent(Context context, Bundle bundle){
        ReactUtils.startTracing(MP_PAYMENT_MANAGEMENT_SYSTEM);
        return ReactNativeHowToPayActivity.createApplinkCallingIntent(context, ReactConst.Screen.HOW_TO_PAY, "Cara Pembayaran", bundle);
    }

    public static Intent createApplinkCallingIntent(Context context, String reactScreenName, String pageTitle, Bundle bundle){
        Intent intent = new Intent(context, ReactNativeHowToPayActivity.class);
        bundle.putString(ReactConst.KEY_SCREEN, reactScreenName);
        bundle.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ReactNativeHowToPayFragment.createInstance(getReactNativeProps());
    }

    protected Bundle getReactNativeProps() {
        Bundle bundle = getIntent().getExtras();
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
