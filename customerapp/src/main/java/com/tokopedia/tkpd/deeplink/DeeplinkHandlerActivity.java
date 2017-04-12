package com.tokopedia.tkpd.deeplink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.ride.deeplink.RideDeeplinkModule;
import com.tokopedia.ride.deeplink.RideDeeplinkModuleLoader;

@DeepLinkHandler({
        ConsumerDeeplinkModule.class,
        CoreDeeplinkModule.class,
        InboxDeeplinkModule.class,
        RideDeeplinkModule.class
})
public class DeeplinkHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
                new ConsumerDeeplinkModuleLoader(),
                new CoreDeeplinkModuleLoader(),
                new InboxDeeplinkModuleLoader(),
                new RideDeeplinkModuleLoader()
        );
        if (getIntent() != null) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            deepLinkDelegate.dispatchFrom(this, intent);
            if (getIntent().getExtras() != null)
            {
                Bundle bundle = getIntent().getExtras();
                UnifyTracking.eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
//                NotificationModHandler.clearCacheIfFromNotification(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        }
        finish();
    }
}
