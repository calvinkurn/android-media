package com.tokopedia.tkpd.applink;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.tkpd.R;

/**
 * @author anggaprasetiyo on 7/20/17.
 */

public class AppLinkWebsiteActivity extends BasePresenterActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_TITLEBAR = "EXTRA_TITLEBAR";
    private static final String EXTRA_PARENT_APP_LINK = "EXTRA_PARENT_APP_LINK";
    private static final String KEY_APP_LINK_QUERY_URL = "url";
    private static final String KEY_APP_LINK_QUERY_TITLEBAR = "titlebar";


    private FragmentGeneralWebView fragmentGeneralWebView;

    private String url;
    private boolean showToolbar;

    public static Intent newInstance(Context context, String url, boolean showToolbar) {
        return new Intent(context, AppLinkWebsiteActivity.class)
                .putExtra(EXTRA_URL, url)
                .putExtra(EXTRA_TITLEBAR, showToolbar);
    }

    @SuppressWarnings("unused")
    @DeepLink({Constants.Applinks.WEBVIEW})
    public static Intent getInstanceIntentAppLink(Context context, Bundle extras) {
        String webUrl = extras.getString(
                KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        boolean showToolbar = extras.getBoolean(KEY_APP_LINK_QUERY_TITLEBAR, true);

        if (TextUtils.isEmpty(webUrl)) {
            webUrl = TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL;
        }
        return AppLinkWebsiteActivity.newInstance(context, webUrl, showToolbar);
    }

    @SuppressWarnings("unused")
    @DeepLink({Constants.Applinks.WEBVIEW_PARENT_HOME})
    public static TaskStackBuilder getInstanceIntentAppLinkBackToHome(Context context, Bundle extras) {
        String webUrl = extras.getString(
                KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (context.getApplicationContext() instanceof TkpdCoreRouter) {
            Intent homeIntent = ((TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
            homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            taskStackBuilder.addNextIntent(homeIntent);
        }
        Intent destination = AppLinkWebsiteActivity.newInstance(context, webUrl);
        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        url = extras.getString(EXTRA_URL);
        showToolbar = extras.getBoolean(EXTRA_TITLEBAR, true);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_link_website;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(com.tokopedia.digital.R.id.container);
        if (fragment == null || !(fragment instanceof FragmentGeneralWebView)) {
            fragmentGeneralWebView = FragmentGeneralWebView.createInstance(getEncodedUrl(url),
                    true, showToolbar);
            getFragmentManager().beginTransaction().replace(com.tokopedia.digital.R.id.container,
                    fragmentGeneralWebView).commit();
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onWebViewSuccessLoad() {

    }

    @Override
    public void onWebViewErrorLoad() {

    }

    @Override
    public void onWebViewProgressLoad() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.tokopedia.core2.R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.tokopedia.core2.R.id.menu_home) {
            if (getApplication() instanceof TkpdCoreRouter) {
                Intent intentHome = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
                if (intentHome != null) startActivity(intentHome);
            }
        } else if (item.getItemId() == com.tokopedia.core2.R.id.menu_help) {
            startActivity(InboxRouter.getContactUsActivityIntent(this));
        }
        return super.onOptionsItemSelected(item);
    }

    private static String getEncodedUrl(String url) {
        url = Uri.decode(url);
        return Uri.encode(url);
    }

    @Override
    public void onBackPressed() {
        try {
            if (fragmentGeneralWebView.getWebview().canGoBack()) {
                fragmentGeneralWebView.getWebview().goBack();
            } else {
                if (isTaskRoot() && getApplication() instanceof TkpdCoreRouter) {
                    startActivity(((TkpdCoreRouter) getApplication()).getHomeIntent(this));
                    finish();
                } else {
                    super.onBackPressed();
                }
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }

    @Override
    protected int getContentId() {
        if (com.tokopedia.abstraction.common.utils.GlobalConfig.isCustomerApp()) {
            return com.tokopedia.abstraction.R.layout.activity_base_legacy_light;
        }

        return super.getContentId();
    }
}
