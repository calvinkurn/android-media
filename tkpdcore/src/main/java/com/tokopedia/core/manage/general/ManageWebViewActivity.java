package com.tokopedia.core.manage.general;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;

import butterknife.BindView;

public class ManageWebViewActivity extends BasePresenterActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener {

    private static final String ARG_TITLE = "ARG_TITLE";
    private Uri mUri;
    private String mPageTitle;

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
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mPageTitle);
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
        FragmentGeneralWebView fragment = FragmentGeneralWebView.createInstance(uri);
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

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
