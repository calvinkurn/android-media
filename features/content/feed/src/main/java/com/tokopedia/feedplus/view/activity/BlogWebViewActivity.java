package com.tokopedia.feedplus.view.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.fragment.BlogWebViewFragment;

/**
 * @author by nisie on 5/19/17.
 */

public class BlogWebViewActivity extends BasePresenterActivity {

    public static final String PARAM_URL = "PARAM_URL";

    public static Intent getIntent(Activity activity, String url) {
        Intent intent = new Intent(activity, BlogWebViewActivity.class);
        intent.putExtra(PARAM_URL, url);
        return intent;
    }

    public interface BackButtonListener {
        void onBackPressed();

        boolean canGoBack();
    }

    private BackButtonListener listener;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        BlogWebViewFragment fragment;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentByTag(
                BlogWebViewFragment.class.getSimpleName()) == null) {
            fragment = BlogWebViewFragment.createInstance(bundle);
        } else {
            fragment = (BlogWebViewFragment) getFragmentManager()
                    .findFragmentByTag(BlogWebViewFragment.class.getSimpleName());
        }

        listener = fragment.getBackButtonListener();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else if (listener != null && listener.canGoBack()) {
            listener.onBackPressed();
        } else {
            finish();
        }
    }
}
