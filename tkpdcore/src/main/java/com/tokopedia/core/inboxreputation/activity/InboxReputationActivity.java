package com.tokopedia.core.inboxreputation.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.inboxreputation.adapter.SectionsPagerAdapter;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFragment;
import com.tokopedia.core.inboxreputation.listener.InboxReputationView;
import com.tokopedia.core.inboxreputation.listener.SellerFragmentReputation;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.TkpdFragmentWrapper;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Nisie on 20/01/16.
 */
public class InboxReputationActivity extends DrawerPresenterActivity
        implements InboxReputationView {

    public static final String REVIEW_ALL = "inbox-reputation";
    public static final String REVIEW_PRODUCT = "inbox-reputation-my-product";
    public static final String REVIEW_USER = "inbox-reputation-my-review";
    public static final String GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY";
    private static final int OFFSCREEN_PAGE_LIMIT = 2;
    public static final int TAB_SELLER_REPUTATION_HISTORY = 2;
    TkpdFragmentWrapper sellerReputationFragment;

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;

    private boolean goToReputationHistory;

    @DeepLink(Constants.Applinks.REPUTATION)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, InboxReputationActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        goToReputationHistory = getIntent().getBooleanExtra(GO_TO_REPUTATION_HISTORY, false);
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION;
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
        if (getApplicationContext() != null && getApplicationContext() instanceof SellerFragmentReputation) {
            SellerFragmentReputation applicationContext = (SellerFragmentReputation) getApplicationContext();
            sellerReputationFragment = applicationContext.getSellerReputationFragment(this);
        }
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));

        if (GlobalConfig.isSellerApp()) {
            indicator.addTab(indicator.newTab().setText(getString(R.string.title_my_product_seller)));
            if (sellerReputationFragment != null) {
                indicator.addTab(indicator.newTab().setText(sellerReputationFragment.getHeader()));
            }
            if (goToReputationHistory) {
                viewPager.setCurrentItem(TAB_SELLER_REPUTATION_HISTORY);
            }
        } else {
            if (!SessionHandler.isUserHasShop(this)) {
                indicator.addTab(indicator.newTab().setText(getString(R.string.title_menu_all)));
                indicator.setVisibility(View.GONE);
            } else {
                indicator.addTab(indicator.newTab().setText(getString(R.string.title_menu_all)));
                indicator.addTab(indicator.newTab().setText(getString(R.string.title_my_product)));
                indicator.addTab(indicator.newTab().setText(getString(R.string.title_my_review)));
            }
        }

    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_REVIEW;
    }

    @Override
    public List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (GlobalConfig.isSellerApp()) {
            fragmentList.add(InboxReputationFragment.createInstance(REVIEW_PRODUCT));
            fragmentList.add(sellerReputationFragment.getTkpdFragment());
        } else {
            if (!SessionHandler.isUserHasShop(this)) {
                fragmentList.add(InboxReputationFragment.createInstance(REVIEW_ALL));
            } else {
                fragmentList.add(InboxReputationFragment.createInstance(REVIEW_ALL));
                fragmentList.add(InboxReputationFragment.createInstance(REVIEW_PRODUCT));
                fragmentList.add(InboxReputationFragment.createInstance(REVIEW_USER));
            }
        }
        return fragmentList;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public PagerAdapter getViewPagerAdapter() {
        return new SectionsPagerAdapter(getFragmentManager(), getFragmentList());
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TkpdState.RequestCode.CODE_OPEN_DETAIL_REPUTATION:
                getFragmentManager().findFragmentById(R.id.pager).onActivityResult(requestCode,
                        resultCode, data);
                break;
            default:
                break;
        }
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
}
