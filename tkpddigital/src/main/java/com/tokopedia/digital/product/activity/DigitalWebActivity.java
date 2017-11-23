package com.tokopedia.digital.product.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.digital.R;

/**
 * @author anggaprasetiyo on 5/15/17.
 */
public class DigitalWebActivity extends BasePresenterActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener {

    private static final String EXTRA_URL = "EXTRA_URL";
    private String url;

    private FragmentGeneralWebView fragmentGeneralWebView;

    public static Intent newInstance(Context context, String url) {
        return new Intent(context, DigitalWebActivity.class)
                .putExtra(EXTRA_URL, url);
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
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview_digital_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        fragmentGeneralWebView = FragmentGeneralWebView.createInstance(url);

        if (fragment == null || !(fragment instanceof FragmentGeneralWebView))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    fragmentGeneralWebView).commit();
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
    public boolean onHomeOptionSelected() {
        try {
            if (fragmentGeneralWebView.getWebview().canGoBack()) {
                fragmentGeneralWebView.getWebview().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
        return true;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentGeneralWebView != null && fragmentGeneralWebView.getWebview() != null
                && fragmentGeneralWebView.getWebview().canGoBack()) {
            fragmentGeneralWebView.getWebview().goBack();
        } else if (fragmentGeneralWebView != null && fragmentGeneralWebView.getWebview() != null) {
            fragmentGeneralWebView.getWebview().stopLoading();
            finish();
        } else {
            finish();
        }
    }
}
