package com.tokopedia.imagesearch.data.mapper;

import android.content.Context;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.imagesearch.analytics.ImageSearchTracking;
import com.tokopedia.imagesearch.domain.viewmodel.BadgeItem;
import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;
import com.tokopedia.imagesearch.network.response.ImageSearchProductResponse;
import com.tokopedia.imagesearch.network.response.SearchProductResponse;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sachinbansal on 5/29/18.
 */

public class ImageProductMapper implements Func1<GraphqlResponse, ProductViewModel> {

    @Override
    public ProductViewModel call(GraphqlResponse response) {
        ImageSearchProductResponse searchProductResponse = response.getData(ImageSearchProductResponse.class);
        return convertToProductViewModel(searchProductResponse);
    }

    public ProductViewModel convertToProductViewModel(ImageSearchProductResponse response) {
        SearchProductResponse searchProductResponse = response.getSearchProductResponse();

        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setProductList(convertToProductItemList(searchProductResponse.getData().getProducts()));
        productViewModel.setCategoryFilterModel(convertToCategoryFilterModel(searchProductResponse.getData().getCategories()));
        productViewModel.setQuery(searchProductResponse.getData().getQuery());
        productViewModel.setToken(searchProductResponse.getData().getToken());
        productViewModel.setDynamicFilterModel(response.getDynamicFilterModel());
        productViewModel.setTotalData(searchProductResponse.getHeader().getTotalData());
        productViewModel.setTotalDataText(searchProductResponse.getHeader().getTotalDataText());
        return productViewModel;
    }

    private CategoryFilterModel convertToCategoryFilterModel(List<SearchProductResponse.Data.Categories> categories) {
        List<CategoryFilterModel.Item> itemList = new ArrayList<>();
        for (SearchProductResponse.Data.Categories category : categories) {
            itemList.add(new CategoryFilterModel.Item(category.getName(), category.getId()));
        }
        return new CategoryFilterModel(itemList);
    }

    private List<ProductItem> convertToProductItemList(List<SearchProductResponse.Data.Products> responseProductList) {
        List<ProductItem> productItemList = new ArrayList<>();

        int position = 0;

        for (SearchProductResponse.Data.Products product : responseProductList) {
            position++;
            productItemList.add(convertToProductItem(product, position));
        }

        return productItemList;
    }

    private static ProductItem convertToProductItem(SearchProductResponse.Data.Products product, int position) {
        ProductItem productItem = new ProductItem();
        productItem.setProductID(product.getId());
        productItem.setProductName(product.getName());
        productItem.setImageUrl(product.getImageUrl());
        productItem.setImageUrl700(product.getImageUrl700());
        productItem.setRating(product.getRating());
        productItem.setCountReview(product.getCountReview());
        productItem.setCountCourier(product.getCountCourier());
        productItem.setDiscountPercentage(product.getDiscountPercentage());
        productItem.setOriginalPrice(product.getOriginalPrice());
        productItem.setPrice(product.getPrice());
        productItem.setPriceRange(product.getPriceRange());
        productItem.setShopID(product.getShop().getId());
        productItem.setShopName(product.getShop().getName());
        productItem.setShopCity(product.getShop().getCity());
        productItem.setGoldMerchant(product.getShop().isIsGold());
        productItem.setOfficial(product.getShop().isOfficial());
        productItem.setWishlisted(product.isWishlist());
        productItem.setBadgesList(convertToBadgesItemList(product.getBadges()));
        productItem.setPosition(position);
        productItem.setCategoryID(product.getCategoryId());
        productItem.setCategoryBreadcrumb(product.getCategoryBreadcrumb());
        productItem.setCategoryName(product.getCategoryName());
        return productItem;
    }

    private static List<BadgeItem> convertToBadgesItemList(List<SearchProductResponse.Data.Products.Badges> badgesList) {
        List<BadgeItem> badgeItemList = new ArrayList<>();

        for (SearchProductResponse.Data.Products.Badges badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private static BadgeItem convertToBadgeItem(SearchProductResponse.Data.Products.Badges badgeModel) {
        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }
}

