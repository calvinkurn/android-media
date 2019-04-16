package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsSuggestionBidInteractionTypeDef;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.DataSuggestions;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostExistingGroupFragment extends TopAdsEditCostFragment<TopAdsDetailEditGroupPresenter, TopAdsDetailGroupViewModel, GroupAd> {

    public static Fragment createInstance(String adId, GroupAd groupAd) {
        Fragment fragment = new TopAdsEditCostExistingGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, groupAd);
        fragment.setArguments(bundle);
        return fragment;
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
    protected void onClickedNext() {
        if(!isPriceError()) {
            super.onClickedNext();
            if (detailAd != null) {
                daggerPresenter.saveAd(detailAd);
                trackingEditCostTopads();
            } else {
                hideLoading();
            }
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        setSuggestionBidText(adFromIntent.getDatum().getMedian(), adFromIntent.getDatum().getMedianFmt());
    }

    @Override
    protected void loadAd(TopAdsDetailGroupViewModel detailAd) {
        super.loadAd(detailAd);
        detailAd.setSuggestionBidValue(suggestionBidValue);
        detailAd.setSuggestionBidButton(TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_NOT_IMPLEMENTED);
        defaultSuggestionBidButtonStatus = TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_NOT_IMPLEMENTED;
    }

    @Override
    protected void onSuggestionBidClicked() {
        super.onSuggestionBidClicked();
        detailAd.setSuggestionBidButton(TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_IMPLEMENTED);
    }

    @Override
    protected void onPriceChanged(double number) {
        super.onPriceChanged(number);
        if (suggestionBidValue != number) {
            detailAd.setSuggestionBidButton(defaultSuggestionBidButtonStatus);
        }
    }

    private void trackingEditCostTopads() {
        if(detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductEditGrupCost(getActivity(), AppEventTracking.EventLabel.BUDGET_PER_DAY);
        }else{
            UnifyTracking.eventTopAdsProductEditGrupCost(getActivity(), AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    @Override
    protected void loadSuggestionBid() {
        List<DataSuggestions> suggestions = new ArrayList<>();
        suggestions.add(new DataSuggestions(TopAdsNetworkConstant.BID_INFO_TYPE_GROUP,
                Arrays.asList(Integer.parseInt(adFromIntent.getId()))));
        daggerPresenter.getBidInfo(TopAdsNetworkConstant.BID_INFO_TYPE_GROUP,
                suggestions, TopAdsNetworkConstant.SOURCE_EDIT_COST_GROUP);
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        // Do nothing
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* this is empty just to deal with abstraction */
    }

    @Override
    public void onBidInfoSuccess(MinimumBidDomain.TopadsBidInfo bidInfo) {
        setSuggestionBidText(bidInfo.getData().get(0));
        detailAd.setSuggestionBidValue(suggestionBidValue);
        detailAd.setSuggestionBidButton(TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_NOT_IMPLEMENTED);
        defaultSuggestionBidButtonStatus = TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_NOT_IMPLEMENTED;
    }

    @Override
    public void onBidInfoError(@Nullable Throwable t) {

    }
}
