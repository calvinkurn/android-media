package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.SectionsPagerAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationActivity extends DrawerPresenterActivity implements HasComponent {

    public static final String GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY";

    public static final int TAB_WAITING_REVIEW = 1;
    public static final int TAB_MY_REVIEW = 2;
    public static final int TAB_BUYER_REVIEW = 3;

    private static final int OFFSCREEN_PAGE_LIMIT = 3;
    public static final int TAB_SELLER_REPUTATION_HISTORY = 2;
    Fragment sellerReputationFragment;

    ViewPager viewPager;
    TabLayout indicator;

    private boolean goToReputationHistory;

    @DeepLink(Constants.Applinks.REPUTATION)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, InboxReputationActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxReputationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        goToReputationHistory = getIntent().getBooleanExtra(GO_TO_REPUTATION_HISTORY, false);
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
    }

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
        return R.layout.activity_inbox_reputation;
    }

    @Override
    protected void initView() {
        super.initView();

        viewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        if (getApplicationContext() != null
                && getApplicationContext() instanceof ReputationRouter) {
            ReputationRouter applicationContext = (ReputationRouter) getApplicationContext();
            sellerReputationFragment = applicationContext.getReputationHistoryFragment();
        }
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));

        if (!GlobalConfig.isSellerApp()) {
            indicator.addTab(indicator.newTab().setText(getString(R.string
                    .title_tab_waiting_review)));
            indicator.addTab(indicator.newTab().setText(getString(R.string
                    .title_tab_my_review)));
        }

        if (sessionHandler.isUserHasShop(this)) {
            indicator.addTab(indicator.newTab().setText(getString(R.string
                    .title_tab_buyer_review)));
        }

        if (GlobalConfig.isSellerApp()) {
            if (sellerReputationFragment != null) {
                indicator.addTab(indicator.newTab().setText(R.string.title_reputation_history));
            }
            if (goToReputationHistory) {
                viewPager.setCurrentItem(TAB_SELLER_REPUTATION_HISTORY);
            }
        }
    }

    protected SectionsPagerAdapter getViewPagerAdapter() {
        return new SectionsPagerAdapter(getSupportFragmentManager(), getFragmentList(), indicator);
    }

    protected List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (GlobalConfig.isSellerApp()) {
            fragmentList.add(InboxReputationFragment.createInstance(TAB_BUYER_REVIEW));
            fragmentList.add(sellerReputationFragment);
        } else {
            fragmentList.add(InboxReputationFragment.createInstance(TAB_WAITING_REVIEW));
            fragmentList.add(InboxReputationFragment.createInstance(TAB_MY_REVIEW));
            if (sessionHandler.isUserHasShop(this)) {
                fragmentList.add(InboxReputationFragment.createInstance(TAB_BUYER_REVIEW));
            }
        }

        return fragmentList;
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
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_REVIEW;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            finish();
        } else if (isTaskRoot()) {
            startActivity(HomeRouter.getHomeActivity(this));
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
