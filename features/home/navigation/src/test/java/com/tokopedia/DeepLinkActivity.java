package com.tokopedia;

import android.app.Activity;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.tokopedia.navigation.applink.HomeNavigationApplinkModule;
import com.tokopedia.navigation.applink.HomeNavigationApplinkModuleLoader;

@DeepLinkHandler({ HomeNavigationApplinkModule.class})
public class DeepLinkActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
        new HomeNavigationApplinkModuleLoader());
    deepLinkDelegate.dispatchFrom(this);
    finish();
  }
}
