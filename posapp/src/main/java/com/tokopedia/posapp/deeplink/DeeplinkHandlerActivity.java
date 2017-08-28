package com.tokopedia.posapp.deeplink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.posapp.deeplink.PosDeeplinkModuleLoader;

/**
 * Created by okasurya on 8/25/17.
 */

@DeepLinkHandler({
        PosDeeplinkModule.class
})
public class DeeplinkHandlerActivity extends AppCompatActivity {
    public static DeepLinkDelegate getDelegateInstance() {
        return new DeepLinkDelegate (
                new PosDeeplinkModuleLoader()
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeepLinkDelegate deepLinkDelegate = getDelegateInstance();
        if(getIntent() != null) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri applink = intent.getData();
            if(deepLinkDelegate.supportsUri(applink.toString())) {
                deepLinkDelegate.dispatchFrom(this, intent);
            }
        }
        finish();
    }
}
