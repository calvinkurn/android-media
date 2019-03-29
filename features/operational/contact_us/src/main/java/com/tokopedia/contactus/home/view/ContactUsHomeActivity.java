package com.tokopedia.contactus.home.view;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.home.view.fragment.ContactUsHomeFragment;
import com.tokopedia.contactus.home.view.presenter.ContactUsHomeContract;
import com.tokopedia.core.home.fragment.SimpleWebViewWithFilePickerFragment;

/**
 * Created by sandeepgoyal on 02/04/18.
 */

public class ContactUsHomeActivity extends BaseSimpleActivity {

    @DeepLink(ApplinkConst.CONTACT_US_NATIVE)
    public static Intent getContactUsIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ContactUsHomeActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    public static Intent getContactUsHomeIntent(Context context, Bundle extra) {
        Intent intent = new Intent(context, ContactUsHomeActivity.class);
        intent.putExtras(extra);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        if (!isNative()) {
            String url = getIntent().getStringExtra(ContactUsConstant.EXTRAS_PARAM_URL);
            if (url != null && url.length() > 0) {
                return SimpleWebViewWithFilePickerFragment.createInstance(url);
            } else {
                return SimpleWebViewWithFilePickerFragment.createInstance(((ContactUsModuleRouter) getApplication()).getContactUsBaseURL());
            }
        } else {
            return ContactUsHomeFragment.newInstance();
        }
    }

    private boolean isNative() {
        return ((ContactUsModuleRouter) getApplication()).getBooleanRemoteConfig(ContactUsHomeContract.CONTACT_US_WEB, false);
    }

    @Override
    public void onBackPressed() {
        if (!isNative()) {
            try {
                SimpleWebViewWithFilePickerFragment webViewFragment = (SimpleWebViewWithFilePickerFragment) getFragment();
                if (webViewFragment != null && webViewFragment.getWebview().canGoBack()) {
                    webViewFragment.getWebview().goBack();
                } else {
                    super.onBackPressed();
                }
            } catch (Exception e) {
                super.onBackPressed();
            }
        } else
            super.onBackPressed();
    }
}
