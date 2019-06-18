package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.topads.R;
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
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostWithoutGroupFragment extends TopAdsEditCostFragment<TopAdsDetailEditProductPresenter, TopAdsDetailProductViewModel, GroupAd> {

    @Inject
    TopAdsDetailNewProductPresenter topAdsDetailNewProductPresenter;
    private int productId;

    public static Fragment createInstance(String adId, int productId) {
        Fragment fragment = new TopAdsEditCostWithoutGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putInt(TopAdsExtraConstant.EXTRA_PRODUCT_ID, productId);
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
        topAdsDetailNewProductPresenter.attachView(this);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        productId = arguments.getInt(TopAdsExtraConstant.EXTRA_PRODUCT_ID, 0);
    }

    @Override
    protected void onClickedNext() {
        if (!isPriceError()) {
            super.onClickedNext();
            if (detailAd != null) {
                trackingEditCostTopads();
                daggerPresenter.saveAd(detailAd);
            }
        }
    }

    private void trackingEditCostTopads() {
        if (detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductEditProductCost(getActivity(), AppEventTracking.EventLabel.BUDGET_PER_DAY);
        } else {
            UnifyTracking.eventTopAdsProductEditProductCost(getActivity(), AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    protected TopAdsDetailProductViewModel initiateDetailAd() {
        return new TopAdsDetailProductViewModel();
    }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
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
    public void onDestroy() {
        super.onDestroy();
        topAdsDetailNewProductPresenter.detachView();
    }

    @Override
    protected void loadSuggestionBid() {
        List<DataSuggestions> suggestions = new ArrayList<>();
        suggestions.add(new DataSuggestions(TopAdsNetworkConstant.BID_INFO_TYPE_PRODUCT, Arrays.asList(productId)));
        topAdsDetailNewProductPresenter.getBidInfo(TopAdsNetworkConstant.BID_INFO_TYPE_PRODUCT,
                suggestions, TopAdsNetworkConstant.SOURCE_NEW_COST_GROUP);
    }

}
