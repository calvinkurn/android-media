package com.tokopedia.tkpd.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactRootView;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeActivity;

/**
 * @author ricoharisin .
 */
public class ReactNativeThankYouPageActivity2 extends ReactNativeActivity {


    public static Intent createReactNativeThankYouPageActivity2(Context context, String reactScreenName, String title) {
        Intent intent = new Intent(context, ReactNativeThankYouPageActivity2.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactRootView = new ReactRootView(this);
        Bundle initialProps = getIntent().getExtras();
        initialProps.putString(ReactConst.KEY_SCREEN, "thankyou-page");
        initialProps.remove("android.intent.extra.REFERRER");
        reactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
        reactRootView.startReactApplication(reactInstanceManager, ReactConst.MAIN_MODULE, initialProps);
        setContentView(reactRootView);
    }

}
