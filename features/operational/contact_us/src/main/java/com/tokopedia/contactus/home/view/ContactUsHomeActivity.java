package com.tokopedia.contactus.home.view;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.home.view.fragment.ContactUsHomeFragment;
import com.tokopedia.contactus.home.view.presenter.ContactUsHomeContract;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity;
import com.tokopedia.core.home.fragment.SimpleWebViewWithFilePickerFragment;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;

/**
 * Created by sandeepgoyal on 02/04/18.
 */

public class ContactUsHomeActivity extends BaseSimpleActivity {

    public static final String URL_HELP = TokopediaUrl.Companion.getInstance().getWEB() + "help?utm_source=android";
    public static final String URL_KEY = "url";
    private RemoteConfig remoteConfig;

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
            String uri = getIntent().getData().getQueryParameter(URL_KEY);
            if (url != null && url.length() > 0) {
                return SimpleWebViewWithFilePickerFragment.createInstance(url);
            } else if (uri!=null && uri.length() > 0) {
                return SimpleWebViewWithFilePickerFragment.createInstance(uri);
            } else {
                return SimpleWebViewWithFilePickerFragment.createInstance(URL_HELP);
            }
        } else {
            return ContactUsHomeFragment.newInstance();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initRemoteConfig();
        super.onCreate(savedInstanceState);
    }

    private boolean isNative() {
        return remoteConfig.getBoolean(ContactUsHomeContract.CONTACT_US_WEB, false);
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contactus_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if(id==R.id.action_inbox){
                startActivity(InboxListActivity.getCallingIntent(this));
                return true;
            }
            return super.onOptionsItemSelected(item);
    }
}
