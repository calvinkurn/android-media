package com.tokopedia.mitra.applink;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;

@DeepLinkHandler({ MitraAppApplinkModule.class})
public class MitraDeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
