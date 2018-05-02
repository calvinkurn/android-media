package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
        if (fragment.getStatisticsOptionLabelView() != null) {
            showCaseList.add(new ShowCaseObject(fragment.getStatisticsOptionLabelView(),
                    getString(R.string.topads_showcase_home_title_7),
                    getString(R.string.topads_showcase_home_desc_7),
                    ShowCaseContentPosition.UNDEFINED,
                    R.color.white));
        }

        if (fragment.getDateLabelView() != null){
            showCaseList.add(new ShowCaseObject(fragment.getDateLabelView(),
                    getString(R.string.topads_showcase_home_title_4),
                    getString(R.string.topads_showcase_home_desc_4),
                    ShowCaseContentPosition.UNDEFINED,
                    R.color.white));
        }
        if (fragment.getContentStatisticsView() != null){
            showCaseList.add(new ShowCaseObject(fragment.getContentStatisticsView(),
                    getString(R.string.topads_showcase_home_title_5),
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
        if (fragment.getItemSummaryLabelView() != null){
            showCaseList.add(new ShowCaseObject(fragment.getItemSummaryLabelView(),
                    getString(R.string.topads_showcase_home_title_1),
                    getString(R.string.topads_showcase_home_desc_1),
                    ShowCaseContentPosition.UNDEFINED,
                    R.color.white, fragment.getScrollView()));
        }
        if (fragment.getKeywordLabelView() != null){
            showCaseList.add(new ShowCaseObject(fragment.getKeywordLabelView(),
                    getString(R.string.topads_showcase_home_title_9),
                    getString(R.string.topads_showcase_home_desc_9),
                    ShowCaseContentPosition.UNDEFINED,
                    R.color.white, fragment.getScrollView()));
        }
        if (fragment.getStoreLabelView() != null){
            showCaseList.add(new ShowCaseObject(fragment.getStoreLabelView(),
                    getString(R.string.topads_showcase_home_title_2),
                    getString(R.string.topads_showcase_home_desc_2),
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

        showCaseDialog.show(this, showCaseTag, showCaseList);
    }
}
