package com.tokopedia.sellerapp.deeplink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.digital.applink.DigitalApplinkModule;
import com.tokopedia.digital.applink.DigitalApplinkModuleLoader;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkAnalyticsImpl;

/**
 * @author rizkyfadillah on 26/07/17.
 */
@DeepLinkHandler({
        DigitalApplinkModule.class
})
public class DeepLinkHandlerActivity extends AppCompatActivity {

    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate(
                new DigitalApplinkModuleLoader()
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = getDelegateInstance();
        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri applink = intent.getData();
            presenter.processUTM(applink);
            deepLinkDelegate.dispatchFrom(this, intent);
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                UnifyTracking.eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        }
        finish();
    }
}
