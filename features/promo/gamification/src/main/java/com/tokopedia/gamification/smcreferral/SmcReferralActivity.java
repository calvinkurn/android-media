package com.tokopedia.gamification.smcreferral;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.webview.BaseSessionWebViewFragment;

public class SmcReferralActivity extends BaseSimpleActivity {

    private static final String KEY_APP_LINK_QUERY_URL = "url";
    private static final String DEFAULT_TOKOPEDIA_WEBSITE_URL = "https://www.tokopedia.com/";
    private static final int LOGIN_REQUEST_CODE = 100;
    private static final String EXTRA_URL = "EXTRA_URL";
    private boolean loginSuccess = false;
    private String url;

    @DeepLink(ApplinkConst.SMC_REFERRAL)
    public static Intent getcallingIntent(Context context, Bundle extras) {
        String webUrl = extras.getString(
                KEY_APP_LINK_QUERY_URL, DEFAULT_TOKOPEDIA_WEBSITE_URL);
        if (TextUtils.isEmpty(webUrl)) {
            webUrl = DEFAULT_TOKOPEDIA_WEBSITE_URL;
        }
        return SmcReferralActivity.newInstance(context, webUrl);
    }

    public static Intent newInstance(Context context, String url) {
        return new Intent(context, SmcReferralActivity.class)
                .putExtra(EXTRA_URL, url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getUrl();
        UserSessionInterface userSession = new UserSession(this);
        if (userSession.isLoggedIn()) {
            loadFragmentForUrl();
        } else {
            Intent loginIntent = ((GamificationRouter) getApplicationContext()).getLoginIntent();
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
        }
    }

    private String getUrl() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            return getIntent().getExtras().getString(EXTRA_URL, "");
        }
        return "";
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (loginSuccess) {
            loadFragmentForUrl();
        }
    }

    private void loadFragmentForUrl() {
        getSupportFragmentManager().beginTransaction()
                .replace(com.tokopedia.abstraction.R.id.parent_view, BaseSessionWebViewFragment.newInstance(url), getTagFragment())
                .commit();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            loginSuccess = true;
        }else{
            finish();
        }
    }
}
