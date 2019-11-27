package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.ProductViewModel;
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecommendationViewModelMapper {

    public List<RecommendationItemViewModel> convertToRecommendationItemViewModel(RecommendationWidget recommendationWidget) {
        List<RecommendationItemViewModel> recommendationItemViewModels = new ArrayList<>();
        for (RecommendationItem recommendationItem : recommendationWidget.getRecommendationItemList()){
            recommendationItemViewModels.add(new RecommendationItemViewModel(recommendationItem));
        }
        return recommendationItemViewModels;
    }


    private List<ProductItemViewModel> convertToProductItemViewModelList(int lastProductItemPositionFromCache, List<RecommendationItem> recommendationItems) {
        List<ProductItemViewModel> productItemList = new ArrayList<>();

        int position = lastProductItemPositionFromCache;

        for (RecommendationItem productModel : recommendationItems) {
            position++;
            productItemList.add(convertToProductItem(productModel, position));
        }

        return productItemList;
    }

    private ProductItemViewModel convertToProductItem(RecommendationItem productModel, int position) {
        ProductItemViewModel productItem = new ProductItemViewModel();
        productItem.setProductID(String.valueOf(productModel.getProductId()));
        productItem.setWarehouseID("");
        productItem.setProductName(productModel.getName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setRating(productModel.getRating());
        productItem.setCountReview(productModel.getCountReview());
        productItem.setDiscountPercentage(productModel.getDiscountPercentageInt());
        productItem.setOriginalPrice(productModel.getSlashedPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setShopID(String.valueOf(productModel.getShopId()));
        productItem.setShopName(productModel.getShopName());
        productItem.setShopCity(productModel.getLocation());
        productItem.setGoldMerchant(productModel.isGold());
        productItem.setWishlisted(productModel.isWishlist());
        productItem.setBadgesList(convertToBadgesItemList(productModel.getBadgesUrl()));
        productItem.setPosition(position);
        productItem.setCategoryBreadcrumb(productModel.getCategoryBreadcrumbs());
        productItem.setIsShopPowerBadge(productModel.getBadgesUrl().contains("power_merchant"));
        productItem.setIsShopOfficialStore(productModel.getBadgesUrl().contains("official_store"));
        productItem.setTopadsClickUrl(productModel.getTrackerImageUrl());
        productItem.setCategoryName(productModel.getRecommendationType());
        return productItem;
    }

    private List<BadgeItemViewModel> convertToBadgesItemList(List<String> badgesList) {
        List<BadgeItemViewModel> badgeItemList = new ArrayList<>();

        for (String badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private BadgeItemViewModel convertToBadgeItem(String badgeModel) {
        BadgeItemViewModel badgeItem = new BadgeItemViewModel();
        badgeItem.setImageUrl(badgeModel);
        badgeItem.setTitle("");
        badgeItem.setShown(true);
        return badgeItem;
    }
}
