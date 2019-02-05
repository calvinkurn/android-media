package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.activity.TopAdsCreatePromoShopActivity;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewScheduleShopFragment extends TopAdsNewScheduleFragment<TopAdsCreatePromoShopModel,
        TopAdsDetailShopViewModel, TopAdsDetailNewShopPresenter>{

    public static final String EXTRA_NEW_SHOP = "EXTRA_NEW_SHOP";

    @Override
    protected void initialVar() {
        super.initialVar();
        if(stepperModel != null){
            loadAd(stepperModel.getTopAdsDetailShopViewModel());
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
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        trackerScheduleShop();
        if (stepperModel == null) {
            stepperModel = new TopAdsCreatePromoShopModel();
        }
        stepperModel.setDetailShopScheduleViewModel(detailAd);
        if(getActivity() != null && getActivity() instanceof TopAdsCreatePromoShopActivity && ((TopAdsCreatePromoShopActivity) getActivity()).getShopAd() != null){
            String shopId = ((TopAdsCreatePromoShopActivity) getActivity()).getShopAd().getShopId();
            stepperModel.getTopAdsDetailShopViewModel().setShopId(Long.valueOf(shopId));
        }

        daggerPresenter.saveAd(stepperModel.getTopAdsDetailShopViewModel());
    }

    private void trackerScheduleShop() {
        if(detailAd.isScheduled()){
            UnifyTracking.eventTopAdsShopAddPromoShowTime(getActivity(), AppEventTracking.EventLabel.SHOWTIME_SETUP);
        }else{
            UnifyTracking.eventTopAdsShopAddPromoShowTime(getActivity(), AppEventTracking.EventLabel.SHOWTIME_AUTO);
        }
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        setResultAdSaved(topAdsDetailAdViewModel);
        if(stepperListener != null) {
            stepperListener.finishPage();
        }
    }

    private void setResultAdSaved(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NEW_SHOP, topAdsDetailAdViewModel.getId());
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        if(topAdsDetailAdViewModel != null && topAdsDetailAdViewModel instanceof TopAdsDetailShopViewModel){
            intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT,
                    ((TopAdsDetailShopViewModel)topAdsDetailAdViewModel).isEnoughDeposit());
        }
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        /* just deal with abstraction */
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* just deal with abstraction */
    }
}
