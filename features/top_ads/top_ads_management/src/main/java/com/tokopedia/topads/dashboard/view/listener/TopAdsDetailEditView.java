package com.tokopedia.topads.dashboard.view.listener;

import androidx.annotation.Nullable;

import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.data.model.response.TopAdsDepositResponse;
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;

/**
 * Created by zulfikarrahman on 2/17/17.
 */
public interface TopAdsDetailEditView extends TopAdsGetProductDetailView {

    void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel);

    void onLoadDetailAdError(String errorMessage);

    void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel);

    void onBalanceCheck(TopAdsDepositResponse.Data topAdsDepositResponse);

    void onSaveAdError(String errorMessage);

    void onBidInfoSuccess(MinimumBidDomain.TopadsBidInfo bidInfo);

    void onBidInfoError(@Nullable Throwable t);
}
