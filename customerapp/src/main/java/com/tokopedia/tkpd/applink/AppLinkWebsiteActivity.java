package com.tokopedia.tkpd.applink;

import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.tkpd.R;
import com.tokopedia.webview.BaseSessionWebViewFragment;

/**
 * @author anggaprasetiyo on 7/20/17.
 * Refer to BaseSimpleWebViewActivity in the libraries.
 */
@Deprecated
public class AppLinkWebsiteActivity extends BasePresenterActivity {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_REFRESH_FLAG = "EXTRA_REFRESH_FLAG";
    private static final String EXTRA_PARENT_APP_LINK = "EXTRA_PARENT_APP_LINK";
    private static final String KEY_APP_LINK_QUERY_URL = "url";
    private static final String KEY_APP_LINK_QUERY_TITLEBAR = "titlebar";
    private static final String KEY_APP_LINK_QUERY_NEED_LOGIN = "need_login";
    private static final String KEY_APP_LINK_QUERY_ALLOW_OVERRIDE = "allow_override";

    private BaseSessionWebViewFragment fragmentWebView;

    private String url;
    private boolean showToolbar;
    private boolean needLogin;
    private boolean allowOverride;

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

        try {
            showToolbar = Boolean.parseBoolean(extras.getString(KEY_APP_LINK_QUERY_TITLEBAR,
                    "true"));
        } catch (ParseException e) {
            showToolbar = true;
        }

        try {
            needLogin = Boolean.parseBoolean(extras.getString(KEY_APP_LINK_QUERY_NEED_LOGIN,
                    "false"));
        } catch (ParseException e) {
            needLogin = false;
        }

        try {
            allowOverride = Boolean.parseBoolean(extras.getString(KEY_APP_LINK_QUERY_ALLOW_OVERRIDE,
                    "true"));
        } catch (ParseException e) {
            allowOverride = true;
        }
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
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof BaseSessionWebViewFragment)) {
            fragmentWebView = BaseSessionWebViewFragment.newInstance(url,needLogin,
                    allowOverride);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    fragmentWebView).commit();
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
            if (fragmentWebView.getWebView().canGoBack()) {
                fragmentWebView.getWebView().goBack();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(PersistentCacheManager.instance.get("reload_webview", int.class, 0) == 1) {
            PersistentCacheManager.instance.put("reload_webview", 0);
            if (fragmentWebView != null) {
                fragmentWebView.reloadPage();
            }
        }
    }
}
