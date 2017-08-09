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
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.home.HomeRouter;
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
    protected void setupToolbar() {
        super.setupToolbar();
        setTheme(R.style.WebViewActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (parentView != null) {
                    parentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                window.setStatusBarColor(getResources().getColor(R.color.white, null));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(com.tokopedia.core.R.drawable.ic_webview_back_button);

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
            startActivity(HomeRouter.getHomeActivity(this));
        } else if (item.getItemId() == com.tokopedia.core.R.id.menu_help) {
            startActivity(InboxRouter.getContactUsActivityIntent(this));
        }
        return super.onOptionsItemSelected(item);
    }

}
