package com.tokopedia.tkpd.applink;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.tkpd.R;

/**
 * @author anggaprasetiyo on 7/20/17.
 */

public class AppLinkWebsiteActivity extends BasePresenterActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String KEY_APP_LINK_QUERY_URL = "url";

    private String url;

    public static Intent newInstance(Context context, String url) {
        return new Intent(context, AppLinkWebsiteActivity.class)
                .putExtra(EXTRA_URL, url);
    }

    @SuppressWarnings("unused")
    @DeepLink({Constants.Applinks.WEBVIEW})
    public static Intent getInstanceIntentAppLink(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (extras.getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent;
            if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(context);
            } else {
                homeIntent = HomeRouter.getHomeActivity(context);
            }
            homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            taskStackBuilder.addNextIntent(homeIntent);
        }
        String webUrl = extras.getString(
                KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        Intent destination = AppLinkWebsiteActivity.newInstance(context, webUrl);
        taskStackBuilder.addNextIntent(destination);
        return destination;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (parentView != null) {
                    parentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                window.setStatusBarColor(getResources().getColor(com.tokopedia.digital.R.color.white, null));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(com.tokopedia.digital.R.color.colorPrimaryDark));
                }
            }
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_webview_back_button);

        toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
        toolbar.setTitleTextAppearance(this, com.tokopedia.core.R.style.WebViewToolbarText);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        url = extras.getString(EXTRA_URL);
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
        if (fragment == null || !(fragment instanceof FragmentGeneralWebView))
            getFragmentManager().beginTransaction().replace(com.tokopedia.digital.R.id.container,
                    FragmentGeneralWebView.createInstance(Uri.encode(url), true)).commit();
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
}
