package com.tokopedia.core.manage.general;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;

import butterknife.BindView;

public class ManageWebViewActivity extends BasePresenterActivity<ManageWebViewContract.Presenter>
        implements FragmentGeneralWebView.OnFragmentInteractionListener {

    private static final String ARG_TITLE = "ARG_TITLE";
    private Uri mUri;
    private String mPageTitle;
    private ManageWebViewPresenter mPresenter;

    public static Intent getCallingIntent(Activity activity, String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        Intent intent = new Intent(activity, ManageWebViewActivity.class);
        intent.setData(Uri.parse(url));
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {
        mUri = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        mPageTitle = extras.getString(ARG_TITLE, getString(R.string.manage_default_page_title));
    }

    @Override
    protected void initialPresenter() {
        mPresenter = new ManageWebViewPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_web_view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            toolbar.removeAllViews();
            setSupportActionBar(toolbar);
            if (getSupportActionBar()!=null) {
                getSupportActionBar().setTitle(mPageTitle);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        openWebView(mUri.toString());
    }

    private void openWebView(String uri) {
        boolean enableJs = true;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            enableJs = false;
        }
        FragmentGeneralWebView fragment = FragmentGeneralWebView.createInstanceJsEnabled(uri, enableJs);
        getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public String getScreenName() {
        return mPageTitle;
    }

    @Override
    public void onWebViewSuccessLoad() {

    }

    @Override
    public void onWebViewErrorLoad() {
        Toast.makeText(this, getString(R.string.error_unknown), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onWebViewProgressLoad() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
