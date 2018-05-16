package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.utils.ApplinkUtils;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment;
import com.tokopedia.topads.dashboard.view.listener.OneUseGlobalLayoutListener;

import java.util.ArrayList;

/**
 * Created by hadi.putra on 23/04/2018.
 */
public class TopAdsDashboardActivity extends DrawerPresenterActivity implements HasComponent<TopAdsComponent>, TopAdsDashboardFragment.Callback{
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
            return ApplinkUtils.getSellerAppApplinkIntent(context, extras);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TopAdsDashboardFragment(), TAG).commit();
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopAdsDashboardActivity.class);
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_TOP_ADS;
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
        return R.layout.activity_simple_fragment;
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
    public TopAdsComponent getComponent() {
        return TopAdsComponentInstance.getComponent(getApplication());
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
                        R.color.white));
            }
            if (fragment.getContentStatisticsView() != null) {
                showCaseList.add(new ShowCaseObject(fragment.getContentStatisticsView(),
                        getString(R.string.topads_showcase_home_title_7),
                        getString(R.string.topads_showcase_home_desc_5),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white));
            }

            if (fragment.getGroupSummaryLabelView() != null){
                showCaseList.add(new ShowCaseObject(fragment.getGroupSummaryLabelView(),
                        getString(R.string.topads_showcase_home_title_8),
                        getString(R.string.topads_showcase_home_desc_8),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white, fragment.getScrollView()));
            }
            if (fragment.getViewGroupPromo() != null){
                showCaseList.add(new ShowCaseObject(fragment.getViewGroupPromo(),
                        getString(R.string.topads_showcase_home_title_1),
                        getString(R.string.topads_showcase_home_desc_1),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white, fragment.getScrollView()));
            }

            if (fragment.getButtonAddPromo() != null){
                showCaseList.add(new ShowCaseObject(fragment.getButtonAddPromo(),
                        getString(R.string.topads_showcase_home_title_6),
                        getString(R.string.topads_showcase_home_desc_6),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white));
            }

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_detail_promo_title_1),
                            getString(R.string.topads_showcase_detail_promo_desc_1),
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
}
