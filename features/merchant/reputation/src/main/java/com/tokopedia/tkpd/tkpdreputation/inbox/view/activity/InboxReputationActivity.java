package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.base.presentation.BaseTemporaryDrawerActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.SectionsPagerAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationActivity extends BaseTemporaryDrawerActivity implements HasComponent {

    public static final String GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY";

    public static final int TAB_WAITING_REVIEW = 1;
    public static final int TAB_MY_REVIEW = 2;
    public static final int TAB_BUYER_REVIEW = 3;
    public static final int TAB_SELLER_REPUTATION_HISTORY = 2;
    private static final int OFFSCREEN_PAGE_LIMIT = 3;
    private Fragment sellerReputationFragment;

    private static final int MARGIN_TAB = 8;
    private static final int MARGIN_START_END_TAB = 16;


    private ViewPager viewPager;
    private TabLayout indicator;
    private UserSessionInterface userSession;

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
        userSession = new UserSession(this);
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
    }

    @Override
    protected void setupURIPass(Uri data) { }

    @Override
    protected void setupBundlePass(Bundle extras) { }

    @Override
    protected void initialPresenter() { }

    @Override
    protected boolean isLightToolbarThemes() {
        return false;
    }

    @Override
    protected int getContentId() {
        if (GlobalConfig.isSellerApp())
            return super.getContentId();
        return R.layout.layout_tab_secondary;
    }

    @Override
    protected int getLayoutId() {
        if (GlobalConfig.isSellerApp())
            return R.layout.activity_inbox_reputation;
        return 0;
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

        if (userSession.hasShop()) {
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

        wrapTabIndicatorToTitle(indicator, (int) CommonUtils.DptoPx(getApplicationContext(), MARGIN_START_END_TAB), (int) CommonUtils.DptoPx(getApplicationContext(), MARGIN_TAB));
    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                tabView.setMinimumWidth(0);
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }
            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
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
            if (userSession.hasShop()) {
                fragmentList.add(InboxReputationFragment.createInstance(TAB_BUYER_REVIEW));
            }
        }

        return fragmentList;
    }

    @Override
    protected void setViewListener() { }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() { }

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
    public BaseAppComponent getComponent() {
        return getBaseAppComponent();
    }
}
