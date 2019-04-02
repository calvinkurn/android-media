package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsSuggestionBidInteractionTypeDef;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.model.TopAdsCreatePromoWithoutGroupModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsNewCostWithoutGroupFragment extends TopAdsNewCostFragment<TopAdsCreatePromoWithoutGroupModel, TopAdsDetailGroupViewModel> implements TopAdsDetailEditView {
    public static final String EXTRA_NEW_PRODUCT_ID = "EXTRA_NEW_PRODUCT_ID";

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
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void loadSuggestionBid() {
        // get id from view model
        List<String> ids = new ArrayList<>();
        for (TopAdsProductViewModel topAdsProductViewModel : stepperModel.getTopAdsProductViewModels()) {
            ids.add(topAdsProductViewModel.getDepartmentId() + "");
        }

        topAdsDetailNewProductPresenter.getSuggestionBid(ids, TopAdsNetworkConstant.SOURCE_NEW_COST_WITHOUT_GROUP);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd.setShopId(Long.parseLong(SessionHandler.getShopID(getActivity())));
        detailAd.setType(TopAdsNetworkConstant.TYPE_PRODUCT_STAT);
    }

    @Override
    protected void onClickedNext() {
        if (!isPriceError()) {
            super.onClickedNext();
            if (stepperModel == null) {
                stepperModel = new TopAdsCreatePromoWithoutGroupModel();
            }
            stepperModel.setDetailGroupCostViewModel(detailAd);
            topAdsDetailNewProductPresenter.saveAd(stepperModel.getDetailProductViewModel(), new ArrayList<>(stepperModel.getTopAdsProductViewModels()),
                    stepperModel.getSource());
        }
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        //do nothing
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        //do nothing
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        if (stepperListener != null) {
            trackingNewCostTopads();
            hideLoading();
            setResultAdSaved(topAdsDetailAdViewModel);
            stepperListener.finishPage();
        }
    }

    private void setResultAdSaved(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        Intent intent = new Intent();
        if(topAdsDetailAdViewModel != null && topAdsDetailAdViewModel instanceof TopAdsDetailShopViewModel){
            intent.putExtra(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, ((TopAdsDetailShopViewModel)topAdsDetailAdViewModel).isEnoughDeposit());
            intent.putExtra(EXTRA_NEW_PRODUCT_ID, topAdsDetailAdViewModel.getId());
        }
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void trackingNewCostTopads() {
        if (detailAd != null && detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductAddPromoWithoutGroupStep2(getActivity(), AppEventTracking.EventLabel.BUDGET_PER_DAY);
        } else {
            UnifyTracking.eventTopAdsProductAddPromoWithoutGroupStep2(getActivity(), AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
        showSnackBarError(errorMessage);
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
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        // do nothing
    }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    protected void showSnackBarError(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAdsDetailNewProductPresenter.detachView();
    }
}
