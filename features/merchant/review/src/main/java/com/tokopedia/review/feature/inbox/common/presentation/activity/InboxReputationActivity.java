package com.tokopedia.review.feature.inbox.common.presentation.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback;
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.header.HeaderUnify;
import com.tokopedia.review.R;
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringListener;
import com.tokopedia.review.common.util.ReviewConstants;
import com.tokopedia.review.common.util.ReviewUtil;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTrackingConstant;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.SectionsPagerAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFragment;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.GlobalMainTabSelectedListener;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationListener;
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants;
import com.tokopedia.review.feature.inbox.common.presentation.listener.OnTabChangeListener;
import com.tokopedia.review.feature.inboxreview.presentation.fragment.InboxReviewFragment;
import com.tokopedia.review.feature.reputationhistory.view.fragment.SellerReputationFragment;
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment;
import com.tokopedia.unifycomponents.TabsUnify;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationActivity extends BaseActivity implements HasComponent, InboxReputationListener, ReviewSellerPerformanceMonitoringListener {

    public static final String GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY";
    public static final String GO_TO_BUYER_REVIEW = "GO_TO_BUYER_REVIEW";
    public static final String IS_DIRECTLY_GO_TO_RATING = "is_directly_go_to_rating";
    public static final String GO_TO_INBOX_REVIEW = "GO_TO_INBOX_REVIEW";

    public static final int TAB_WAITING_REVIEW = 1;
    public static final int TAB_MY_REVIEW = 2;
    public static final int TAB_BUYER_REVIEW = 3;
    public static final int TAB_SELLER_REPUTATION_HISTORY = 2;
    public static final int TAB_SELLER_INBOX_REVIEW = 1;
    private Fragment sellerReputationFragment;
    private Fragment reviewSellerFragment;
    private Fragment inboxReviewFragment;

    private static final int MARGIN_TAB = 8;
    private static final int MARGIN_START_END_TAB = 16;
    private static final String SELLER_INBOX_REVIEW_TAB = "inbox-ulasan";
    public static String tickerTitle;

    private ViewPager viewPager;
    public TabsUnify indicator;
    private PagerAdapter sectionAdapter;
    private HeaderUnify toolbar;

    private UserSessionInterface userSession;

    private boolean goToReputationHistory;
    private boolean goToBuyerReview;
    private boolean goToInboxReview;
    private boolean canFireTracking;
    private ReputationTracking reputationTracking;
    private boolean isAppLinkProccessed = false;

    private PageLoadTimePerformanceInterface pageLoadTimePerformance;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxReputationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        goToReputationHistory = getIntent().getBooleanExtra(GO_TO_REPUTATION_HISTORY, false);
        goToBuyerReview = getIntent().getBooleanExtra(GO_TO_BUYER_REVIEW, false);
        goToInboxReview = getIntent().getBooleanExtra(GO_TO_INBOX_REVIEW, false);
        String tab = getIntent().getData().getQueryParameter(ReviewInboxConstants.PARAM_TAB);
        String source = getIntent().getData().getQueryParameter(ReviewInboxConstants.PARAM_SOURCE);
        canFireTracking = !goToReputationHistory;
        userSession = new UserSession(this);
        reputationTracking = new ReputationTracking();
        super.onCreate(savedInstanceState);
        if (!GlobalConfig.isSellerApp()) {
            startActivity(ReviewInboxActivity.Companion.createNewInstance(this, tab, source));
            finish();
        }
        startPerformanceMonitoring();
        setContentView(R.layout.activity_inbox_reputation);
        setupStatusBar();
        clearCacheIfFromNotification();
        initView(tab);
        setupTabViewpager(tab);
        openBuyerReview();
    }

    private void initView(String tab) {
        viewPager = findViewById(R.id.pager_reputation);
        indicator = findViewById(R.id.indicator_unify);
        toolbar = findViewById(R.id.headerInboxReputation);
        indicator.getUnifyTabLayout().clearOnTabSelectedListeners();
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        setupToolbar();
        if (GlobalConfig.isSellerApp()) {
            reviewSellerFragment = RatingProductFragment.Companion.createInstance();
            Bundle reviewSellerBundle = new Bundle();
            if(isExistParamTab(tab)) {
                reviewSellerBundle.putBoolean(IS_DIRECTLY_GO_TO_RATING, goToReputationHistory);
            } else {
                reviewSellerBundle.putBoolean(IS_DIRECTLY_GO_TO_RATING, !goToReputationHistory);
            }
            reviewSellerFragment.setArguments(reviewSellerBundle);
            inboxReviewFragment = InboxReviewFragment.Companion.createInstance();
            sellerReputationFragment = SellerReputationFragment.createInstance();
        }
    }

    private void setupTabViewpager(String tab) {
        indicator.setCustomTabMode(TabLayout.MODE_SCROLLABLE);
        indicator.setCustomTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator.getUnifyTabLayout()));
        final int[] selectedTabPosition = {indicator.getTabLayout().getSelectedTabPosition()};
        indicator.getUnifyTabLayout().addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager, this) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int position = tab.getPosition();
                if (position != selectedTabPosition[0]) {
                    List<Fragment> fragmentList = getFragmentList();
                    for (int i = 0; i < fragmentList.size(); i++) {
                        Fragment fragment= fragmentList.get(i);
                        if(fragment instanceof InboxReviewFragment) {
                            OnTabChangeListener onTabChangeListener = (OnTabChangeListener) fragmentList.get(i);
                            onTabChangeListener.onTabChange(position);
                        }
                    }
                    selectedTabPosition[0] = position;
                }
                if (!canFireTracking) {
                    canFireTracking = true;
                    return;
                }
                if (tickerTitle != null) {
                    reputationTracking.onSuccessGetIncentiveOvoTracker(tickerTitle, ReputationTrackingConstant.WAITING_REVIEWED);
                }
            }
        });

        setupTabName();

        sectionAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getFragmentList(), indicator.getUnifyTabLayout());
        viewPager.setOffscreenPageLimit(getFragmentList().size());
        viewPager.setAdapter(sectionAdapter);

        if (GlobalConfig.isSellerApp()) {
            if (isExistParamTab(tab)) {
                if (tab.equals(SELLER_INBOX_REVIEW_TAB)) {
                    viewPager.setCurrentItem(TAB_SELLER_INBOX_REVIEW);
                }
            }
        }

        if (goToReputationHistory) {
            viewPager.setCurrentItem(TAB_SELLER_REPUTATION_HISTORY);
        }

        if (goToBuyerReview) {
            viewPager.setCurrentItem(TAB_BUYER_REVIEW);
        }

        wrapTabIndicatorToTitle(indicator.getUnifyTabLayout(), (int) ReviewUtil.INSTANCE.DptoPx(this, MARGIN_START_END_TAB), (int) ReviewUtil.INSTANCE.DptoPx(this, MARGIN_TAB));
    }

    private boolean isExistParamTab(String tab) {
        return tab != null && !tab.isEmpty();
    }

    private void setupTabName() {
        if (!GlobalConfig.isSellerApp()) {
            indicator.addNewTab(getString(R.string
                    .title_tab_waiting_review));
            indicator.addNewTab(getString(R.string
                    .title_tab_my_review));
        }

        if (GlobalConfig.isSellerApp()) {
            if (reviewSellerFragment != null) {
                indicator.addNewTab(getString(R.string.title_rating_product));
            }
            if (inboxReviewFragment != null) {
                indicator.addNewTab(getString(R.string.title_review_inbox));
            }
        }

        if (userSession.hasShop()) {
            indicator.addNewTab(getString(R.string
                    .title_tab_buyer_review));
        }

        if (GlobalConfig.isSellerApp()) {
            if (sellerReputationFragment != null) {
                indicator.addNewTab(getString(R.string.title_reputation_history));
            }
        }
    }

    private void openBuyerReview() {
        if (!isAppLinkProccessed && getIntent() != null && getIntent().getData() != null) {
            String featureName = getIntent().getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME);
            if (featureName != null && !featureName.isEmpty()) {
                isAppLinkProccessed = true;
                int buyerReviewFragmentPosition = findBuyerReviewFragmentPosition();
                if (buyerReviewFragmentPosition != -1) {
                    viewPager.setCurrentItem(buyerReviewFragmentPosition);
                }
            }
        }
    }

    private int findBuyerReviewFragmentPosition() {
        for (int i = 0; i < sectionAdapter.getCount(); i++) {
            Fragment fragment = ((SectionsPagerAdapter) sectionAdapter).getItem(i);
            if (fragment instanceof InboxReputationFragment && ((InboxReputationFragment) fragment).getTab() == TAB_BUYER_REVIEW)
                return i;
        }
        return -1;
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

    public List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (GlobalConfig.isSellerApp()) {
            fragmentList.add(reviewSellerFragment);
            fragmentList.add(inboxReviewFragment);
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
    public void onBackPressed() {
        if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            RouteManager.route(this, ApplinkConst.SellerApp.SELLER_APP_HOME);
            finish();
        } else if (isTaskRoot()) {
            RouteManager.route(this, ApplinkConst.HOME);
            finish();
        }
        tickerTitle = null;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.title_activity_reputation_review));
    }

    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        }
    }

    private void clearCacheIfFromNotification() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ReviewInboxConstants.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(ReviewInboxConstants.EXTRA_FROM_PUSH, false)) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                LocalCacheHandler.clearCache(this, ReviewInboxConstants.GCM_NOTIFICATION);
            }
        }
    }

    @Override
    public void updateTickerTitle(@NotNull String title) {
        tickerTitle = title;
    }

    @Override
    public void startPerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            pageLoadTimePerformance = new PageLoadTimePerformanceCallback(
                    ReviewConstants.RATING_PRODUCT_PLT_PREPARE_METRICS,
                    ReviewConstants.RATING_PRODUCT_PLT_NETWORK_METRICS,
                    ReviewConstants.RATING_PRODUCT_PLT_RENDER_METRICS,
                    0,
                    0,
                    0,
                    0,
                    null
            );
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.startMonitoring(ReviewConstants.RATING_PRODUCT_TRACE);
                pageLoadTimePerformance.startPreparePagePerformanceMonitoring();
            }
        }
    }

    @Override
    public void stopPerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.stopMonitoring();
                pageLoadTimePerformance = null;
            }
        }
    }

    @Override
    public void startPreparePagePerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.startPreparePagePerformanceMonitoring();
            }
        }
    }

    @Override
    public void stopPreparePagePerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.stopPreparePagePerformanceMonitoring();
            }
        }
    }

    @Override
    public void startNetworkRequestPerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.startNetworkRequestPerformanceMonitoring();
            }
        }
    }

    @Override
    public void stopNetworkRequestPerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.stopNetworkRequestPerformanceMonitoring();
            }
        }
    }

    @Override
    public void startRenderPerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.startRenderPerformanceMonitoring();
            }
        }
    }

    @Override
    public void stopRenderPerformanceMonitoring() {
        if (GlobalConfig.isSellerApp()) {
            if (pageLoadTimePerformance != null) {
                pageLoadTimePerformance.stopRenderPerformanceMonitoring();
            }
        }
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }
}