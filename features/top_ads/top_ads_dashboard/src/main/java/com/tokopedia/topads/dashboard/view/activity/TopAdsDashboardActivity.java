package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.topads.common.data.util.ApplinkUtil;
import com.tokopedia.topads.dashboard.R;
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant;
import com.tokopedia.topads.dashboard.data.utils.ShowCaseDialogFactory;
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent;
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment;
import com.tokopedia.topads.common.view.listener.OneUseGlobalLayoutListener;

import java.util.ArrayList;

/**
 * Created by hadi.putra on 23/04/2018.
 */
public class TopAdsDashboardActivity extends BaseSimpleActivity implements HasComponent<TopAdsDashboardComponent>, TopAdsDashboardFragment.Callback{
    public static final String TAG = TopAdsDashboardActivity.class.getSimpleName();

    private ShowCaseDialog showCaseDialog;

    @DeepLink(ApplinkConst.SellerApp.TOPADS_DASHBOARD)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        if (GlobalConfig.isSellerApp()) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            return getCallingIntent(context)
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            return ApplinkUtil.getSellerAppApplinkIntent(context, extras);
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsDashboardActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionSendAnalyticsIfFromPushNotif();
    }

    private void actionSendAnalyticsIfFromPushNotif() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TopAdsDashboardConstant.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(TopAdsDashboardConstant.EXTRA_FROM_PUSH, false)) {
                // TODO USE ROUTER TO SEND GTM
                /*UnifyTracking.eventOpenTopadsPushNotification(
                        getIntent().getStringExtra(UnifyTracking.EXTRA_LABEL)
                );*/
            }
        }
    }


    @Override
    public TopAdsDashboardComponent getComponent() {
        return DaggerTopAdsDashboardComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH, false)) {
                // TODO GET HOME INTENT
                /*Intent homeIntent = ((TkpdCoreRouter)getApplication()).getHomeIntent(this);
                startActivity(homeIntent);*/
                finish();
            } /*else
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
                }*/
        }
        super.onBackPressed();
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsDashboardActivity.class.getName();

        TopAdsDashboardFragment fragment = (TopAdsDashboardFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment == null){
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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar.getHeight() > 0) {
            int height = toolbar.getHeight();
            int width = toolbar.getWidth();

            if (fragment.getShopInfoLayout() != null) {
                showCaseList.add(new ShowCaseObject(fragment.getShopInfoLayout(),
                        getString(R.string.topads_showcase_home_title_3),
                        getString(R.string.topads_showcase_home_desc_3),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white, fragment.getScrollView()));
            }
            if (fragment.isContentVisible()) {
                if (fragment.getContentStatisticsView() != null) {
                    showCaseList.add(new ShowCaseObject(fragment.getContentStatisticsView(),
                            getString(R.string.topads_showcase_home_title_7),
                            getString(R.string.topads_showcase_home_desc_5),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white, fragment.getScrollView()));
                }

                if (fragment.getGroupSummaryLabelView() != null) {
                    showCaseList.add(new ShowCaseObject(fragment.getGroupSummaryLabelView(),
                            getString(R.string.topads_showcase_home_title_8),
                            getString(R.string.topads_showcase_home_desc_8),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white, fragment.getScrollView()));
                }
                if (fragment.getViewGroupPromo() != null) {
                    showCaseList.add(new ShowCaseObject(fragment.getViewGroupPromo(),
                            getString(R.string.topads_showcase_home_title_1),
                            getString(R.string.topads_showcase_home_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white, fragment.getScrollView()));
                }
            }

            if (fragment.getButtonAddPromo() != null) {
                showCaseList.add(new ShowCaseObject(fragment.getButtonAddPromo(),
                        getString(R.string.topads_showcase_home_title_6),
                        getString(R.string.topads_showcase_home_desc_6),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white));
            }

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_help),
                            getString(R.string.topads_showcase_detail_help),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int) (height * 0.8), 0, width, height}));

            showCaseDialog.show(this, showCaseTag, showCaseList);
        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(
                    toolbar,
                    new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            startShowCase();
                        }
                    }
            ));
        }


    }

    @Override
    protected Fragment getNewFragment() {
        return TopAdsDashboardFragment.createInstance();
    }
}
