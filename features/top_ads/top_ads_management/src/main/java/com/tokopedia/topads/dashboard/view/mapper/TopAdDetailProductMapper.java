package com.tokopedia.topads.dashboard.view.mapper;

import android.text.TextUtils;

import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 2/24/2017.
 */

public class TopAdDetailProductMapper {

    private static final String VALUE_TRUE = "1";
    private static final String VALUE_FALSE = "0";

    public static TopAdsDetailShopViewModel convertDomainToView(TopAdsDetailProductDomainModel domainModel) {
        TopAdsDetailShopViewModel viewModel = new TopAdsDetailShopViewModel();
        viewModel.setId(Long.parseLong(domainModel.getAdId()));
        viewModel.setType(Integer.parseInt(domainModel.getAdType()));
        viewModel.setGroupId(Long.parseLong(domainModel.getGroupId()));
        viewModel.setShopId(Long.parseLong(domainModel.getShopId()));
        viewModel.setItemId(Long.parseLong(domainModel.getItemId()));
        viewModel.setStatus(Integer.parseInt(domainModel.getStatus()));
        viewModel.setPriceBid(domainModel.getPriceBid());
        if (!TextUtils.isEmpty(domainModel.getAdBudget())) {
            viewModel.setBudget(VALUE_TRUE.equalsIgnoreCase(domainModel.getAdBudget()));
        }
        viewModel.setPriceDaily(domainModel.getPriceDaily());
        viewModel.setStickerId(Integer.valueOf(domainModel.getStickerId()));
        if (!TextUtils.isEmpty(domainModel.getAdSchedule())) {
            viewModel.setScheduled(VALUE_TRUE.equalsIgnoreCase(domainModel.getAdSchedule()));
        }
        viewModel.setStartDate(domainModel.getAdStartDate());
        viewModel.setStartTime(domainModel.getAdStartTime());
        viewModel.setEndDate(domainModel.getAdEndDate());
        viewModel.setEndTime(domainModel.getAdEndTime());
        viewModel.setImage(domainModel.getAdImage());
        viewModel.setTitle(domainModel.getAdTitle());
        viewModel.setEnoughDeposit(domainModel.isEnoughDeposit());
        return viewModel;
    }

    public static TopAdsDetailShopDomainModel convertViewToDomain(TopAdsDetailProductViewModel viewModel) {
        TopAdsDetailShopDomainModel domainModel = new TopAdsDetailShopDomainModel();
        domainModel.setAdId(String.valueOf(viewModel.getId()));
        domainModel.setAdType(String.valueOf(viewModel.getType()));
        domainModel.setGroupId(String.valueOf(viewModel.getGroupId()));
        domainModel.setShopId(String.valueOf(viewModel.getShopId()));
        domainModel.setItemId(String.valueOf(viewModel.getItemId()));
        domainModel.setStatus(String.valueOf(viewModel.getStatus()));
        domainModel.setPriceBid(viewModel.getPriceBid());
        domainModel.setAdBudget(viewModel.isBudget() ? VALUE_TRUE : VALUE_FALSE);
        domainModel.setPriceDaily(viewModel.getPriceDaily());
        domainModel.setStickerId(String.valueOf(viewModel.getStickerId()));
        domainModel.setAdSchedule(viewModel.isScheduled() ? VALUE_TRUE : VALUE_FALSE);
        domainModel.setAdStartDate(viewModel.getStartDate());
        domainModel.setAdStartTime(viewModel.getStartTime());
        domainModel.setAdEndDate(viewModel.getEndDate());
        domainModel.setAdEndTime(viewModel.getEndTime());
        domainModel.setAdImage(viewModel.getImage());
        domainModel.setAdTitle(viewModel.getTitle());
        domainModel.setSuggestionBidValue(viewModel.getSuggestionBidValue());
        domainModel.setSuggestionBidButton(viewModel.getSuggestionBidButton());
        return domainModel;
    }

    public static List<TopAdsDetailProductDomainModel> convertViewToDomainList(TopAdsDetailProductViewModel detailAd, ArrayList<TopAdsProductViewModel> topAdsProductList) {
        List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels = new ArrayList<>();
        for(TopAdsProductViewModel topAdsProductViewModel : topAdsProductList){
            detailAd.setItemId(topAdsProductViewModel.getId());
            topAdsDetailProductDomainModels.add(convertViewToDomain(detailAd));
        }
        return topAdsDetailProductDomainModels;
    }

}
