package com.tokopedia.digital.product.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
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
<<<<<<< f_rizky_add_access_to_digital_product_for_seller_app
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

}
