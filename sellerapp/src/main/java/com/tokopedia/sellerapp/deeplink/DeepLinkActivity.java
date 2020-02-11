package com.tokopedia.sellerapp.deeplink;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.deeplink.listener.DeepLinkView;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkPresenter;
import com.tokopedia.sellerapp.deeplink.presenter.DeepLinkPresenterImpl;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public class DeepLinkActivity extends BasePresenterActivity<DeepLinkPresenter> implements DeepLinkView, DeepLinkWebViewHandleListener {
    private Uri uriData;
    private Bundle extras;
    private static final String EXTRA_STATE_APP_WEB_VIEW = "EXTRA_STATE_APP_WEB_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDeepLink();
    }

    @Override
    public void catchToWebView(String url) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_view, BaseSessionWebViewFragment.newInstance(url))
                .commit();
    }

    @Override
    public void inflateFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().add(R.id.main_view, fragment, tag).commit();
    }

    @Override
    public void inflateFragmentV4(androidx.fragment.app.Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, fragment, tag).commit();
    }

    @Override
    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_view, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void hideActionBar() {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void setupURIPass(Uri data) {
        uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new DeepLinkPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deeplink_viewer;
    }

    @Override
    protected void initView() {

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
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDeepLink() {
        if (uriData != null || getIntent().getBooleanExtra(EXTRA_STATE_APP_WEB_VIEW, false)) {
            presenter.processDeepLinkAction(uriData);
        }
    }

}
