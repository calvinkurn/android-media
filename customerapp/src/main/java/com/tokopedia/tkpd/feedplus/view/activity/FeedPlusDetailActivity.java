package com.tokopedia.tkpd.feedplus.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.feedplus.view.fragment.FeedPlusDetailFragment;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;

import static com.tokopedia.discovery.activity.BrowseProductActivity.EXTRA_DATA;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailActivity extends BasePresenterActivity {

    public static final String EXTRA_DATA = "EXTRA_DATA";

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
        if(getIntent().getExtras()!= null)
            bundle.putAll(getIntent().getExtras());

        FeedPlusDetailFragment fragment = FeedPlusDetailFragment.createInstance(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(FeedPlusDetailFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else
            fragmentTransaction.replace(R.id.container,
                    getSupportFragmentManager().findFragmentByTag(
                            FeedPlusDetailFragment.class.getSimpleName()));
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

    public static Intent getIntent(FragmentActivity activity, ProductCardViewModel productCardViewModel) {
        Intent intent = new Intent(activity, FeedPlusDetailActivity.class);
        intent.putExtra(EXTRA_DATA, productCardViewModel);
        return intent;
    }
}
