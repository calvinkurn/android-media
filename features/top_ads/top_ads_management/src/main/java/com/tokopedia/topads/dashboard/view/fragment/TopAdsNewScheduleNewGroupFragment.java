package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewScheduleNewGroupFragment extends TopAdsNewScheduleFragment<TopAdsCreatePromoNewGroupModel,
        TopAdsDetailGroupViewModel, TopAdsDetailNewGroupPresenter> implements TopAdsDetailNewGroupView{

    public static final String EXTRA_NEW_GROUP_ID = "EXTRA_NEW_GROUP_ID";
    public static final String EXTRA_IS_ENOUGH_DEPOSIT = "EXTRA_IS_ENOUGH_DEPOSIT";

    @Override
    protected void initView(View view) {
        super.initView(view);
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        if(stepperModel != null){
            loadAd(stepperModel.getDetailAd());
        }
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
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        if (stepperModel == null) {
            stepperModel = new TopAdsCreatePromoNewGroupModel();
        }
        trackingNewScheduleTopads();
        stepperModel.setDetailGroupScheduleViewModel(detailAd);
        daggerPresenter.saveAdNew(stepperModel.getGroupName(), stepperModel.getDetailAd(), stepperModel.getTopAdsProductViewModels(), stepperModel.getSource());
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        if(stepperListener != null) {
            stepperListener.finishPage();
        }
    }

    @Override
    protected Intent setMoreResulAdSaved(Intent intent, TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        if(topAdsDetailAdViewModel != null && topAdsDetailAdViewModel instanceof TopAdsDetailGroupViewModel){
            intent.putExtra(EXTRA_NEW_GROUP_ID, ((TopAdsDetailGroupViewModel)topAdsDetailAdViewModel).getGroupId());
            intent.putExtra(EXTRA_IS_ENOUGH_DEPOSIT, ((TopAdsDetailGroupViewModel)topAdsDetailAdViewModel).isEnoughDeposit());
        }
        return intent;
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        /* just deal with abstraction */
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* just deal with abstraction */
    }

    private void trackingNewScheduleTopads() {
        if(detailAd != null && detailAd.isScheduled()) {
            UnifyTracking.eventTopAdsProductAddPromoStep3(getActivity(), AppEventTracking.EventLabel.SHOWTIME_SETUP);
        }else{
            UnifyTracking.eventTopAdsProductAddPromoStep3(getActivity(), AppEventTracking.EventLabel.SHOWTIME_AUTO);
        }
    }


    @Override
    public void goToGroupDetail(String groupId) {

    }

}
