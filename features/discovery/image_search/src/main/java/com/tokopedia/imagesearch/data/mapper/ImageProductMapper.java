package com.tokopedia.imagesearch.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.imagesearch.domain.model.BadgeModel;
import com.tokopedia.imagesearch.domain.model.LabelModel;
import com.tokopedia.imagesearch.domain.model.ProductModel;
import com.tokopedia.imagesearch.domain.model.SearchResultModel;
import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;
import com.tokopedia.imagesearch.network.response.ImageSearchProductResponse;
import com.tokopedia.imagesearch.network.response.SearchProductResponse;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sachinbansal on 5/29/18.
 */

public class ImageProductMapper implements Func1<GraphqlResponse, SearchResultModel> {

    @Override
    public SearchResultModel call(GraphqlResponse response) {
        ImageSearchProductResponse searchProductResponse = response.getData(ImageSearchProductResponse.class);
        return mappingPojoIntoDomain(searchProductResponse.getSearchProductResponse());
    }

    public SearchResultModel mappingPojoIntoDomain(SearchProductResponse searchProductResponse) {
        SearchResultModel model = new SearchResultModel();
        model.setTotalData(searchProductResponse.getHeader().getTotalData());
        model.setAdditionalParams(searchProductResponse.getHeader().getAdditionalParams());

        model.setProductList(mappingProduct(searchProductResponse.getData().getProducts()));
        model.setQuery(searchProductResponse.getData().getQuery());
        model.setShareUrl(searchProductResponse.getData().getShareUrl());
        model.setCategoryFilterModel(mappingCategoryFilterModel(searchProductResponse.getData().getCategories()));

        return model;
    }

    private CategoryFilterModel mappingCategoryFilterModel(List<SearchProductResponse.Data.Categories> categories) {
        List<CategoryFilterModel.Item> filterItemList = new ArrayList<>();

        for(SearchProductResponse.Data.Categories category : categories) {
            filterItemList.add(new CategoryFilterModel.Item(category.getName(), category.getId()));
        }

        return new CategoryFilterModel(filterItemList);
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

