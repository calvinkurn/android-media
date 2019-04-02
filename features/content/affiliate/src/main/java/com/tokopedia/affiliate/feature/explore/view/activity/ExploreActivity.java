package com.tokopedia.affiliate.feature.explore.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.fragment.ExploreFragment;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.applink.ApplinkConst;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreActivity extends BaseActivity {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @DeepLink(ApplinkConst.AFFILIATE_EXPLORE)
    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, ExploreActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        inflateFragment();
    }

    protected int getLayoutRes() {
        return R.layout.activity_no_toolbar;
    }

    protected void inflateFragment() {
        Fragment newFragment = getNewFragment();
        if (newFragment == null) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newFragment, getTagFragment())
                .commit();
    }

    protected Fragment getNewFragment() {
        return ExploreFragment.getInstance(getIntent().getExtras());
    }

    protected Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag(getTagFragment());
    }

    protected String getTagFragment() {
        return TAG_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        if (getFragment() instanceof ExploreContract.View) {
            ExploreContract.View view = (ExploreContract.View) getFragment();

            if (view.shouldBackPressed()) {
                super.onBackPressed();
            } else {
                view.refresh();
            }
        } else {
            super.onBackPressed();
        }
    }
}
