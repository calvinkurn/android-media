package com.tokopedia.contactus.home.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.home.view.presenter.ContactUsHomeContract;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.webview.BaseSessionWebViewFragment;
import com.tokopedia.webview.BaseWebViewFragment;

/**
 * Created by sandeepgoyal on 02/04/18.
 */

public class ContactUsHomeActivity extends BaseSimpleActivity {

    public static final String URL_HELP = TokopediaUrl.Companion.getInstance().getWEB() + "help?utm_source=android";

    @Override
    protected Fragment getNewFragment() {
        String url = getIntent().getStringExtra(ContactUsConstant.EXTRAS_PARAM_URL);
        if (url != null && url.length() > 0) {
            return BaseSessionWebViewFragment.newInstance(url);
        } else {
            return BaseSessionWebViewFragment.newInstance(URL_HELP);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        try {
            BaseWebViewFragment webViewFragment = (BaseWebViewFragment) getFragment();
            if (webViewFragment != null && webViewFragment.getWebView().canGoBack()) {
                webViewFragment.getWebView().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SplitCompat.installActivity(this);
        getMenuInflater().inflate(R.menu.contactus_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_inbox) {
            startActivity(InboxListActivity.getCallingIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
