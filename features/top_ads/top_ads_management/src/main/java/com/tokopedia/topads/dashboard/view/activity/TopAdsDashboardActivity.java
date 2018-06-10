package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardPagerAdapter;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardProductFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardShopFragment;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardTabListener;
import com.tokopedia.topads.common.view.presenter.BaseDatePickerPresenterImpl;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDashboardActivity extends DrawerPresenterActivity implements TopAdsDashboardFragment.Callback {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SnackbarRetry snackbarRetry;
    private TopAdsDashboardShopFragment dashboardShopFragment;
    private TopAdsDashboardProductFragment dashboardProductFragment;
    private BaseDatePickerPresenterImpl datePickerPresenter;
    private TopAdsDashboardTabListener topadsDashList;
    private ShowCaseDialog showCaseDialog;


    @DeepLink(Constants.Applinks.SellerApp.TOPADS_DASHBOARD)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return getCallingIntent(context)
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsDashboardActivity.class);
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        datePickerPresenter = new BaseDatePickerPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_ads_dashboard;
    }

    @Override
    protected void initView() {
        super.initView();
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        datePickerPresenter.resetDate();
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(TopAdsConstant.OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        topadsDashList = new TopAdsDashboardTabListener(viewPager);
        tabLayout.setOnTabSelectedListener(topadsDashList);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.top_ads_title_product));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_top_ads_store));
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(this, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                dashboardShopFragment.loadData();
                dashboardProductFragment.loadData();
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(this, R.color.green_400));
    }

    Fragment getCurrentFragment() {
        return (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_TOP_ADS;
    }

    public PagerAdapter getViewPagerAdapter() {
        dashboardProductFragment = TopAdsDashboardProductFragment.createInstance();
        dashboardProductFragment.setCallback(this);
        dashboardShopFragment = TopAdsDashboardShopFragment.createInstance();
        dashboardShopFragment.setCallback(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(dashboardProductFragment);
        fragmentList.add(dashboardShopFragment);
        return new TopAdsDashboardPagerAdapter(getSupportFragmentManager(), fragmentList);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        actionSendAnalyticsIfFromPushNotif();
    }

    private void actionSendAnalyticsIfFromPushNotif() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)) {
                UnifyTracking.eventOpenTopadsPushNotification(
                        getIntent().getStringExtra(UnifyTracking.EXTRA_LABEL)
                );
            }
        }
    }

    @Override
    public void onLoadDataError() {
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onLoadDataSuccess() {
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onCreditAdded() {
        dashboardShopFragment.populateDeposit();
        dashboardProductFragment.populateDeposit();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
                Intent homeIntent = ((TkpdCoreRouter)getApplication()).getHomeIntent(this);
                startActivity(homeIntent);
                finish();
            } else
                //coming from deeplink
                if (getApplication() instanceof TkpdCoreRouter) {
                    TkpdCoreRouter router = (TkpdCoreRouter) getApplication();
                    try {
                        Intent intent = new Intent(this, router.getHomeClass(this));
                        this.startActivity(intent);
                        this.finish();
                        return;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
        super.onBackPressed();
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsDashboardActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)) {
            return;
        }
        if (showCaseDialog != null) {
            return;
        }
        if (dashboardProductFragment == null) {
            return;
        }
        showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
        showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                return false;
            }
        });

        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
        showCaseList.add(new ShowCaseObject(
                ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0),
                getString(R.string.topads_showcase_home_title_1),
                getString(R.string.topads_showcase_home_desc_1),
                ShowCaseContentPosition.UNDEFINED,
                R.color.tkpd_main_green));

        showCaseList.add(new ShowCaseObject(
                ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1),
                getString(R.string.topads_showcase_home_title_2),
                getString(R.string.topads_showcase_home_desc_2),
                ShowCaseContentPosition.UNDEFINED,
                R.color.tkpd_main_green));

        viewPager.setCurrentItem(0);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                View depositView = dashboardProductFragment.getDepositView();
                View calendarView = dashboardProductFragment.getCalendarView();
                View statisticView = dashboardProductFragment.getStatisticView();
                ViewGroup scrollView = dashboardProductFragment.getScrollView();

                if (depositView != null) {
                    showCaseList.add(new ShowCaseObject(
                            depositView,
                            getString(R.string.topads_showcase_home_title_3),
                            getString(R.string.topads_showcase_home_desc_3),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white,
                            scrollView));
                }

                if (calendarView != null) {
                    showCaseList.add(new ShowCaseObject(
                            calendarView,
                            getString(R.string.topads_showcase_home_title_4),
                            getString(R.string.topads_showcase_home_desc_4),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white,
                            scrollView));
                }

                if (statisticView != null) {
                    showCaseList.add(new ShowCaseObject(
                            statisticView,
                            getString(R.string.topads_showcase_home_title_5),
                            getString(R.string.topads_showcase_home_desc_5),
                            ShowCaseContentPosition.UNDEFINED,
                            0,
                            scrollView));
                }
                showCaseDialog.show(TopAdsDashboardActivity.this, showCaseTag, showCaseList);
            }
        });
    }
}