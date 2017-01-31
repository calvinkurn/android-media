package com.tokopedia.tkpd.deeplink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.core.deeplink.CoreDeeplinkModule;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModule;
import com.tokopedia.seller.deeplink.SellerDeeplinkModule;
import com.tokopedia.transaction.deeplink.TransactionDeeplinkModule;

@DeepLinkHandler({ConsumerDeeplinkModule.class, CoreDeeplinkModule.class, TransactionDeeplinkModule.class,
        InboxDeeplinkModule.class, SellerDeeplinkModule.class})
public class DeeplinkHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
