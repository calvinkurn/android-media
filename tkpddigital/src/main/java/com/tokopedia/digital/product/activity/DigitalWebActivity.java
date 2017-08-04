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
        if (fragment == null || !(fragment instanceof FragmentGeneralWebView))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    FragmentGeneralWebView.createInstance(url, false)).commit();

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
}
