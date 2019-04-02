package com.tokopedia.topads.dashboard.view.fragment;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsSuggestionBidInteractionTypeDef;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoNewGroupModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostNewGroupFragment extends TopAdsNewCostFragment<TopAdsCreatePromoNewGroupModel, TopAdsDetailGroupViewModel> implements TopAdsDetailEditView {

    @Inject
    TopAdsDetailNewProductPresenter topAdsDetailNewProductPresenter;

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        topAdsDetailNewProductPresenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        if (stepperModel != null) {
            loadAd(stepperModel.getDetailAd());
        }
    }

    @Override
    protected void loadSuggestionBid() {
        // get id from view model
        List<String> ids = new ArrayList<>();
        for (TopAdsProductViewModel topAdsProductViewModel : stepperModel.getTopAdsProductViewModels()) {
            ids.add(topAdsProductViewModel.getDepartmentId() + "");
        }
        topAdsDetailNewProductPresenter.getSuggestionBid(ids, TopAdsNetworkConstant.SOURCE_NEW_COST_GROUP);
    }

    @Override
    protected void onClickedNext() {
        if (!isPriceError()) {
            super.onClickedNext();
            if (stepperListener != null) {
                trackingNewCostTopads();
                if (stepperModel == null) {
                    stepperModel = new TopAdsCreatePromoNewGroupModel();
                }
                stepperModel.setDetailGroupCostViewModel(detailAd);
                stepperListener.goToNextPage(stepperModel);
                hideLoading();
            }
        }
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    private void trackingNewCostTopads() {
        if (detailAd != null && detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductAddPromoStep2(getActivity(), AppEventTracking.EventLabel.BUDGET_PER_DAY);
        } else {
            UnifyTracking.eventTopAdsProductAddPromoStep2(getActivity(), AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsDetailNewProductPresenter.detachView();
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        setSuggestionBidText(s);
        detailAd.setSuggestionBidValue(suggestionBidValue);
        detailAd.setSuggestionBidButton(TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_NOT_IMPLEMENTED);
        defaultSuggestionBidButtonStatus = TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_NOT_IMPLEMENTED;
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        detailAd.setSuggestionBidButton(TopAdsSuggestionBidInteractionTypeDef.NO_SUGGESTION);
    }

    @Override
    protected void onSuggestionBidClicked() {
        detailAd.setSuggestionBidButton(TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_IMPLEMENTED);
    }

    @Override
    protected void onPriceChanged(double number) {
        super.onPriceChanged(number);
        if (suggestionBidValue != number) {
            detailAd.setSuggestionBidButton(defaultSuggestionBidButtonStatus);
        }
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) { /* remain empty*/ }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) { /* remain empty*/ }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) { /* remain empty*/ }

    @Override
    public void onLoadDetailAdError(String errorMessage) { /* remain empty*/ }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) { /* remain empty*/ }

    @Override
    public void onSaveAdError(String errorMessage) { /* remain empty*/ }
}
