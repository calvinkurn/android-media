package com.tokopedia.imagesearch.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.imagesearch.domain.model.BadgeModel;
import com.tokopedia.imagesearch.domain.model.LabelModel;
import com.tokopedia.imagesearch.domain.model.ProductModel;
import com.tokopedia.imagesearch.domain.model.SearchResultModel;
import com.tokopedia.imagesearch.network.exception.RuntimeHttpErrorException;
import com.tokopedia.imagesearch.network.response.ImageSearchProductResponse;
import com.tokopedia.imagesearch.network.response.SearchProductResponse;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sachinbansal on 5/29/18.
 */

public class ImageProductMapper implements Func1<GraphqlResponse<ImageSearchProductResponse>, SearchResultModel> {

    @Override
    public SearchResultModel call(GraphqlResponse<ImageSearchProductResponse> response) {
        if (response != null) {
            try {
                ImageSearchProductResponse searchProductResponse = response.getData();
                return mappingPojoIntoDomain(searchProductResponse.getSearchProductResponse());
            } catch (Exception e) {
                throw new RuntimeHttpErrorException(430);
            }
        } else {
            throw new RuntimeHttpErrorException(432);
        }
    }

    public SearchResultModel mappingPojoIntoDomain(SearchProductResponse searchProductResponse) {
        SearchResultModel model = new SearchResultModel();
        model.setTotalData(searchProductResponse.getHeader().getTotalData());
        model.setTotalDataText(searchProductResponse.getHeader().getTotalDataText());
        model.setAdditionalParams(searchProductResponse.getHeader().getAdditionalParams());
        model.setRedirectUrl(searchProductResponse.getData().getRedirection().getRedirectUrl());

        if (!TextUtils.isEmpty(searchProductResponse.getData().getRedirection().getDepartmentId())) {
            model.setDepartmentId(searchProductResponse.getData().getRedirection().getDepartmentId());
        }

        model.setHasCatalog(searchProductResponse.getData().getCatalogs().size() > 0);
        model.setProductList(mappingProduct(searchProductResponse.getData().getProducts()));
        model.setQuery(searchProductResponse.getData().getQuery());
        model.setSource(searchProductResponse.getData().getSource());
        model.setShareUrl(searchProductResponse.getData().getShareUrl());

        if (searchProductResponse.getData().getSuggestionText() != null) {
            model.setSuggestionText(searchProductResponse.getData().getSuggestionText().getText());
            model.setSuggestedQuery(searchProductResponse.getData().getSuggestionText().getQuery());
        }
        if (searchProductResponse.getData().getSuggestionsInstead() != null) {
            model.setSuggestionCurrentKeyword(searchProductResponse.getData().getSuggestionsInstead().getCurrentKeyword());
        }

        return model;
    }

    private List<ProductModel> mappingProduct(List<SearchProductResponse.Data.Products> products) {
        List<ProductModel> list = new ArrayList<>();
        for (SearchProductResponse.Data.Products data : products) {
            ProductModel model = new ProductModel();
            model.setProductID(data.getId());
            model.setProductName(data.getName());
            model.setProductUrl(data.getUrl());
            model.setImageUrl(data.getImageUrl());
            model.setImageUrl700(data.getImageUrl700());
            model.setRating(data.getRating());
            model.setCountReview(data.getCountReview());
            model.setCountCourier(data.getCountCourier());
            model.setDiscountPercentage(data.getDiscountPercentage());
            model.setOriginalPrice(data.getOriginalPrice());
            model.setPrice(data.getPrice());
            model.setPriceRange(data.getPriceRange());
            model.setShopID(data.getShop().getId());
            model.setShopName(data.getShop().getName());
            model.setShopCity(data.getShop().getCity());
            model.setGoldMerchant(data.getShop().isIsGold());
            model.setOfficial(data.getShop().isOfficial());
            model.setLabelList(mappingLabels(data.getLabels()));
            model.setBadgesList(mappingBadges(data.getBadges()));
            model.setFeatured(data.getIsFeatured() == 1);
            model.setTopLabel(isContainItems(data.getTopLabel()) ? data.getTopLabel().get(0) : "");
            model.setBottomLabel(isContainItems(data.getBottomLabel()) ? data.getBottomLabel().get(0) : "");
            model.setCategoryId(data.getCategoryId());
            model.setCategoryName(data.getCategoryName());
            model.setCategoryBreadcrumb(data.getCategoryBreadcrumb());
            list.add(model);
        }
        return list;
    }

    private boolean isContainItems(List list) {
        return list != null && !list.isEmpty();
    }

    private List<LabelModel> mappingLabels(List<SearchProductResponse.Data.Products.Labels> labels) {
        List<LabelModel> list = new ArrayList<>();

        if (labels == null || labels.size() == 0) return list;

        for (SearchProductResponse.Data.Products.Labels data : labels) {
            LabelModel model = new LabelModel();
            model.setTitle(data.getTitle());
            model.setColor(data.getColor());
            list.add(model);
        }
        return list;
    }

    private List<BadgeModel> mappingBadges(List<SearchProductResponse.Data.Products.Badges> badges) {
        List<BadgeModel> list = new ArrayList<>();
        if (badges == null || badges.size() == 0) return list;
        for (SearchProductResponse.Data.Products.Badges data : badges) {
            BadgeModel model = new BadgeModel();
            model.setTitle(data.getTitle());
            model.setImageUrl(data.getImageUrl());
            model.setShown(data.isShown());
            list.add(model);
        }
        return list;
    }
}

