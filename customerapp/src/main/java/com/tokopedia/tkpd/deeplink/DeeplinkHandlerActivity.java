package com.tokopedia.tkpd.deeplink;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.digital.applink.DigitalApplinkModule;
import com.tokopedia.digital.applink.DigitalApplinkModuleLoader;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.seller.applink.SellerApplinkModule;
import com.tokopedia.seller.applink.SellerApplinkModuleLoader;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkAnalyticsImpl;
import com.tokopedia.tkpdpdp.applink.PdpApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModule;
import com.tokopedia.transaction.applink.TransactionApplinkModuleLoader;

@DeepLinkHandler({
        ConsumerDeeplinkModule.class,
        CoreDeeplinkModule.class,
        InboxDeeplinkModule.class,
        SellerApplinkModule.class,
        TransactionApplinkModule.class,
        DigitalApplinkModule.class,
        PdpApplinkModule.class
})
public class DeeplinkHandlerActivity extends AppCompatActivity {

    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate(
                new ConsumerDeeplinkModuleLoader(),
                new CoreDeeplinkModuleLoader(),
                new InboxDeeplinkModuleLoader(),
                new SellerApplinkModuleLoader(),
                new TransactionApplinkModuleLoader(),
                new DigitalApplinkModuleLoader(),
                new PdpApplinkModuleLoader()
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = getDelegateInstance();
        DeepLinkAnalyticsImpl presenter = new DeepLinkAnalyticsImpl();
        if (getIntent() != null) {
            Intent intent = getIntent();
            Uri applink = intent.getData();
            presenter.processUTM(applink);
            deepLinkDelegate.dispatchFrom(this, intent);
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                UnifyTracking.eventPersonalizedClicked(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
//                NotificationModHandler.clearCacheIfFromNotification(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        }
        finish();
    }
}
