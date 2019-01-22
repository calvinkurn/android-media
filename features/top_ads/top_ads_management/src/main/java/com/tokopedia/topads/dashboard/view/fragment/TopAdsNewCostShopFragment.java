package com.tokopedia.topads.dashboard.view.fragment;

import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoShopModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostShopFragment extends TopAdsNewCostFragment<TopAdsCreatePromoShopModel, TopAdsDetailShopViewModel> {
    @Override
    protected void initView(View view) {
        super.initView(view);
        if(stepperModel != null){
            loadAd(stepperModel.getTopAdsDetailShopViewModel());
        }
    }

    @Override
    protected void loadSuggestionBid() {
        // Do nothing
    }

    @Override
    protected void onClickedNext() {
        if(!isPriceError()) {
            super.onClickedNext();
            trackerBudgetShop();
            if (stepperListener != null) {
                if (stepperModel == null) {
                    stepperModel = new TopAdsCreatePromoShopModel();
                }
                stepperModel.setDetailShopCostViewModel(detailAd);
                stepperListener.goToNextPage(stepperModel);
                hideLoading();
            }
        }
    }

    private void trackerBudgetShop() {
        if(detailAd.isBudget()) {
            UnifyTracking.eventTopAdsShopAddPromoBudget(getActivity(), AppEventTracking.EventLabel.BUDGET_PER_DAY);
        }else{
            UnifyTracking.eventTopAdsShopAddPromoBudget(getActivity(), AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }
}
