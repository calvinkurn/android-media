package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.model.StepperModel;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditScheduleExistingGroupFragment extends TopAdsNewScheduleFragment<StepperModel, TopAdsDetailGroupViewModel, TopAdsDetailEditGroupPresenter> {

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditScheduleExistingGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        headerText.setVisibility(View.GONE);
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        if(detailAd != null) {
            trackingEditScheduleTopads();
            daggerPresenter.saveAd(detailAd);
        }
    }

    private void trackingEditScheduleTopads() {
        if(detailAd.isScheduled()) {
            UnifyTracking.eventTopAdsProductEditGrupSchedule(getActivity(), AppEventTracking.EventLabel.SHOWTIME_SETUP);
        }else{
            UnifyTracking.eventTopAdsProductEditGrupSchedule(getActivity(), AppEventTracking.EventLabel.SHOWTIME_AUTO);
        }
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        getActivity().finish();
    }

    @Override
    public void onBidInfoSuccess(MinimumBidDomain.TopadsBidInfo bidInfo) {

    }

    @Override
    public void onBidInfoError(@Nullable Throwable t) {

    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }
}
