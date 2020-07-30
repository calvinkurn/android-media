package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.widget.Toolbar;
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
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.review.feature.inboxreview.presentation.fragment.InboxReviewFragment;
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTrackingConstant;
import com.tokopedia.tkpd.tkpdreputation.constant.Constant;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.SectionsPagerAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.GlobalMainTabSelectedListener;
import com.tokopedia.tkpd.tkpdreputation.utils.ReputationUtil;
import com.tokopedia.unifycomponents.TabsUnify;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;
/**
 * @author by nisie on 8/10/17.
 */

public class  InboxReputationActivity extends BaseActivity implements HasComponent {

    public static final String GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY";
    public static final String IS_DIRECTLY_GO_TO_RATING = "is_directly_go_to_rating";

    public static final int TAB_WAITING_REVIEW = 1;
    public static final int TAB_MY_REVIEW = 2;
    public static final int TAB_BUYER_REVIEW = 3;
    public static final int TAB_SELLER_REPUTATION_HISTORY = 2;
    private static final int OFFSCREEN_PAGE_LIMIT = 3;
    private Fragment sellerReputationFragment;
    private Fragment reviewSellerFragment;
    private Fragment inboxReviewFragment;

    private static final int MARGIN_TAB = 8;
    private static final int MARGIN_START_END_TAB = 16;
    public static String tickerTitle;

    private ViewPager viewPager;
    private TabsUnify indicator;
    private PagerAdapter sectionAdapter;
    private Toolbar toolbar;

    private UserSessionInterface userSession;

    private boolean goToReputationHistory;
    private boolean canFireTracking;
    private ReputationTracking reputationTracking;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxReputationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        goToReputationHistory = getIntent().getBooleanExtra(GO_TO_REPUTATION_HISTORY, false);
        canFireTracking = !goToReputationHistory;
        userSession = new UserSession(this);
        reputationTracking = new ReputationTracking();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_reputation);
        setupStatusBar();
        clearCacheIfFromNotification();
        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.pager_reputation);
        indicator = findViewById(R.id.indicator_unify);
        indicator.getUnifyTabLayout().clearOnTabSelectedListeners();
        toolbar = findViewById(R.id.toolbar);

        setupToolbar();

        if (getApplicationContext() != null
                && getApplicationContext() instanceof ReputationRouter) {
            ReputationRouter applicationContext = (ReputationRouter) getApplicationContext();
            sellerReputationFragment = applicationContext.getReputationHistoryFragment();
            reviewSellerFragment = applicationContext.getReviewSellerFragment();
        }
        if(GlobalConfig.isSellerApp()) {
            reviewSellerFragment = RatingProductFragment.Companion.createInstance();
            Bundle reviewSellerBundle = new Bundle();
            reviewSellerBundle.putBoolean(IS_DIRECTLY_GO_TO_RATING, !goToReputationHistory);
            reviewSellerFragment.setArguments(reviewSellerBundle);
            inboxReviewFragment = InboxReviewFragment.Companion.createInstance();
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator.getUnifyTabLayout()));
        indicator.getUnifyTabLayout().addOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager, this) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (!canFireTracking) {
                    canFireTracking = true;
                    return;
                }
                if(!GlobalConfig.isSellerApp()) {
                    reputationTracking.onTabReviewSelectedTracker(tab.getPosition());
                }
                if(tickerTitle != null) {
                    reputationTracking.onSuccessGetIncentiveOvoTracker(tickerTitle, ReputationTrackingConstant.WAITING_REVIEWED);
                }
            }
        });

        if (!GlobalConfig.isSellerApp()) {
            indicator.addNewTab(getString(R.string
                    .title_tab_waiting_review));
            indicator.addNewTab(getString(R.string
                    .title_tab_my_review));
        }

        if(GlobalConfig.isSellerApp()) {
            if(reviewSellerFragment != null) {
                indicator.addNewTab(getString(R.string.title_rating_product));
            }
            if(inboxReviewFragment != null) {
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

        sectionAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getFragmentList(), indicator.getUnifyTabLayout());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.setAdapter(sectionAdapter);

        if (goToReputationHistory) {
            viewPager.setCurrentItem(TAB_SELLER_REPUTATION_HISTORY);
        }

        wrapTabIndicatorToTitle(indicator.getUnifyTabLayout(), (int) ReputationUtil.DptoPx(this, MARGIN_START_END_TAB), (int) ReputationUtil.DptoPx(this, MARGIN_TAB));
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

    protected List<Fragment> getFragmentList() {
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
        reputationTracking.onBackPressedInboxReviewClickTracker(indicator.getUnifyTabLayout().getSelectedTabPosition());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
        }
    }

    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.tkpdabstraction_green_600));
        }
    }

    private void clearCacheIfFromNotification() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constant.Notification.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(Constant.Notification.EXTRA_FROM_PUSH, false)) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                LocalCacheHandler.clearCache(this, Constant.Notification.GCM_NOTIFICATION);
            }
        }
    }
}