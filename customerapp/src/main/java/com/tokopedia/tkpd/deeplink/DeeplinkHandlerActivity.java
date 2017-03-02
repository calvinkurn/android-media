package com.tokopedia.tkpd.deeplink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.seller.deeplink.SellerDeeplinkModule;
import com.tokopedia.seller.deeplink.SellerDeeplinkModuleLoader;
import com.tokopedia.transaction.deeplink.TransactionDeeplinkModule;
import com.tokopedia.transaction.deeplink.TransactionDeeplinkModuleLoader;

@DeepLinkHandler({ConsumerDeeplinkModule.class, CoreDeeplinkModule.class, TransactionDeeplinkModule.class,
        InboxDeeplinkModule.class, SellerDeeplinkModule.class})
public class DeeplinkHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
                new ConsumerDeeplinkModuleLoader(),
                new CoreDeeplinkModuleLoader(),
                new TransactionDeeplinkModuleLoader(),
                new InboxDeeplinkModuleLoader(),
                new SellerDeeplinkModuleLoader()
        );
        if (getIntent() != null) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            deepLinkDelegate.dispatchFrom(this, intent);
            if (getIntent().getExtras() != null)
            {
                Bundle bundle = getIntent().getExtras();
                NotificationModHandler.clearCacheIfFromNotification(bundle.getString(Constants.EXTRA_APPLINK_CATEGORY));
            }
        }
        finish();
    }
}
