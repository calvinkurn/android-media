package com.tokopedia.tkpd.deeplink.listener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */
public interface DeepLinkView {

    void inflateFragment(Fragment fragment, String tag);

    void inflateFragmentV4(android.support.v4.app.Fragment fragment, String tag);

    void initDeepLink();

    void replaceFragment(Fragment fragment, String tag);

    void hideActionBar();

    void actionChangeToolbarWithBackToNative();

    void goToActivity(Class<? extends Activity> activityClass, Bundle bundle);

    void networkError(Uri uriData);

    void showLoading();

    void finishLoading();

    void goToPage(Intent destination);
}
