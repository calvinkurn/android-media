package com.tokopedia.topads.dashboard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.TopAdsComponentInstance;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.common.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailGroupFragment;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;

public class TopAdsDetailGroupActivity extends BaseSimpleActivity
        implements TopAdsDetailGroupFragment.OnTopAdsDetailGroupListener, HasComponent<TopAdsComponent> {

    private ShowCaseDialog showCaseDialog;

    public static final String TAG = TopAdsDetailGroupFragment.class.getSimpleName();

    private boolean isAdChanged;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void startShowCase() {
        final String showCaseTag = TopAdsDetailGroupActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)) {
            return;
        }
        if (showCaseDialog!= null) {
            return;
        }

        final TopAdsDetailGroupFragment topAdsDetailGroupFragment =
                (TopAdsDetailGroupFragment) getSupportFragmentManager().findFragmentByTag(TopAdsDetailGroupFragment.class.getSimpleName());

        if (topAdsDetailGroupFragment == null || topAdsDetailGroupFragment.getView() == null) {
            return;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar.getHeight() > 0) {
            final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
            int height = toolbar.getHeight();
            int width = toolbar.getWidth();
            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_detail_group_title_1),
                            getString(R.string.topads_showcase_detail_group_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width - (int)(height * 0.8), 0,width, height}));
            View statusView = topAdsDetailGroupFragment.getStatusView();
            if (statusView != null) {
                showCaseList.add(
                        new ShowCaseObject(
                                statusView,
                                getString(R.string.topads_showcase_detail_group_title_2),
                                getString(R.string.topads_showcase_detail_group_desc_2),
                                ShowCaseContentPosition.UNDEFINED,
                                Color.WHITE));
            }

            View productView = topAdsDetailGroupFragment.getProductView();
            if (productView != null) {
                showCaseList.add(
                        new ShowCaseObject(
                                productView,
                                getString(R.string.topads_showcase_detail_group_title_3),
                                getString(R.string.topads_showcase_detail_group_desc_3),
                                ShowCaseContentPosition.UNDEFINED,
                                Color.WHITE));
            }

            showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
            showCaseDialog.show(TopAdsDetailGroupActivity.this, showCaseTag, showCaseList);
        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OneUseGlobalLayoutListener(toolbar, new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    startShowCase();
                }
            }));
        }
    }


    public static Intent createIntent(Context context, String groupId) {
        Intent intent = new Intent(context, TopAdsDetailGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            GroupAd ad = null;
            String adId = null;
            boolean forceRefresh = false;
            boolean isEnoughDeposit = false;
            if (getIntent() != null && getIntent().getExtras() != null) {
                ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
                adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
                forceRefresh = getIntent().getBooleanExtra(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, false);
                isAdChanged = getIntent().getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
                isEnoughDeposit = getIntent().getBooleanExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, false);
            }
            fragment = TopAdsDetailGroupFragment.createInstance(ad, adId, forceRefresh, isEnoughDeposit);
            return fragment;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, isAdChanged);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected String getTagFragment() {
        return TAG;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    public TopAdsComponent getComponent() {
        return TopAdsComponentInstance.getComponent(getApplication());
    }
}
