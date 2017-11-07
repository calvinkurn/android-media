package com.tokopedia.tkpd.applink;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
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
    private static final String EXTRA_PARENT_APP_LINK = "EXTRA_PARENT_APP_LINK";
    private static final String KEY_APP_LINK_QUERY_URL = "url";
    private static final String KEY_APP_LINK_PARENT_APP_LINK = "parent_applink";

    private String url;
    private String parentAppLink;

    public static Intent newInstance(Context context, String url, String parentAppLink) {
        return new Intent(context, AppLinkWebsiteActivity.class)
                .putExtra(EXTRA_URL, url).putExtra(EXTRA_PARENT_APP_LINK, parentAppLink);
    }

    @SuppressWarnings("unused")
    @DeepLink({Constants.Applinks.WEBVIEW})
    public static TaskStackBuilder getInstanceIntentAppLink(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (extras.getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent = HomeRouter.getHomeActivityInterfaceRouter(context);
            homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            taskStackBuilder.addNextIntent(homeIntent);
        }
        String webUrl = extras.getString(
                KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        String parentAppLink = extras.getString(KEY_APP_LINK_PARENT_APP_LINK);

        Intent destination = AppLinkWebsiteActivity.newInstance(context, webUrl, parentAppLink);
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
        parentAppLink = extras.getString(EXTRA_PARENT_APP_LINK);
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

            getFragmentManager().beginTransaction().replace(com.tokopedia.digital.R.id.container,
                    FragmentGeneralWebView.createInstance(getEncodedUrl(url), true)).commit();
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
        inflater.inflate(com.tokopedia.core.R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.tokopedia.core.R.id.menu_home) {
            if (getApplication() instanceof TkpdCoreRouter) {
                Intent intentHome = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
                if (intentHome != null) startActivity(intentHome);
            }
        } else if (item.getItemId() == com.tokopedia.core.R.id.menu_help) {
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
        if (parentAppLink != null && !parentAppLink.isEmpty()) {
            if (getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter) getApplication()).actionApplink(this, parentAppLink);
                finish();
            }
        } else
            super.onBackPressed();
    }
}
