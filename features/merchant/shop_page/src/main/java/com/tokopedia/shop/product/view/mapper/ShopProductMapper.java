package com.tokopedia.shop.product.view.mapper;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.common.util.WishListUtils;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductBadge;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductLabel;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModelOld;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModelOld;
import com.tokopedia.shop.product.view.model.ShopProductListViewModelOld;
import com.tokopedia.shop.product.view.model.ShopProductViewModelOld;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 2/25/18.
 */

public class ShopProductMapper {

    private static final String BADGE_FREE_RETURN = "Free Return";
    private static final String LABEL_CASHBACK = "Cashback";
    private static final String LABEL_PERCENTAGE = "%";

    public List<ShopProductViewModelOld> convertFromShopProduct(List<ShopProduct> shopProductList, int page, int defaultPerPage) {
        List<ShopProductViewModelOld> shopProductViewModelOldList = new ArrayList<>();
        for (int i = 0; i < shopProductList.size(); i++) {
            ShopProduct shopProduct = shopProductList.get(i);
            ShopProductListViewModelOld shopProductViewModel = convertFromShopProduct(shopProduct);
            shopProductViewModel.setPositionTracking(getCurrentPageView(page, i+1, defaultPerPage));
            shopProductViewModelOldList.add(shopProductViewModel);
        }
        return shopProductViewModelOldList;
    }

    private ShopProductListViewModelOld convertFromShopProduct(ShopProduct shopProduct) {
        ShopProductListViewModelOld shopProductViewModel = new ShopProductListViewModelOld();

        shopProductViewModel.setId(shopProduct.getProductId());
        shopProductViewModel.setName(shopProduct.getProductName());
        shopProductViewModel.setDisplayedPrice(shopProduct.getProductPrice());
        shopProductViewModel.setImageUrl(shopProduct.getProductImage());
        shopProductViewModel.setImageUrl300(shopProduct.getProductImage300());
        shopProductViewModel.setImageUrl700(shopProduct.getProductImage700());
        shopProductViewModel.setProductUrl(shopProduct.getProductUrl());
//        shopProductViewModel.setRating(); Api not support
        shopProductViewModel.setPo(TextApiUtils.isValueTrue(shopProduct.getProductPreorder()));
        shopProductViewModel.setTotalReview(shopProduct.getProductReviewCount());
        shopProductViewModel.setWholesale(TextApiUtils.isValueTrue(shopProduct.getProductWholesale()));
        if (shopProduct.getBadges() != null && shopProduct.getBadges().size() > 0) {
            for (ShopProductBadge badge : shopProduct.getBadges()) {
                if (badge.getTitle().equalsIgnoreCase(BADGE_FREE_RETURN)) {
                    shopProductViewModel.setFreeReturn(true);
                    break;
                }
            }
        }
        if (shopProduct.getLabels() != null && shopProduct.getLabels().size() > 0) {
            for (ShopProductLabel shopProductLabel : shopProduct.getLabels()) {
                if (shopProductLabel.getTitle().startsWith(LABEL_CASHBACK)) {
                    String cashbackText = shopProductLabel.getTitle();
                    cashbackText = cashbackText.replace(LABEL_CASHBACK, "");
                    cashbackText = cashbackText.replace(LABEL_PERCENTAGE, "");
                    double cashbackPercentage = Double.parseDouble(cashbackText.trim());
                    shopProductViewModel.setCashback(cashbackPercentage);
                    break;
                }
            }
        }
        shopProductViewModel.setSoldOut(shopProduct.isSoldOutStatus());
        return shopProductViewModel;
    }

    public void mergeShopProductViewModelWithWishList(List<ShopProductViewModelOld> shopProductViewModelOldList, List<String> productWishList, boolean showWishlist) {
        for (ShopProductViewModelOld shopProductViewModelOld : shopProductViewModelOldList) {
            shopProductViewModelOld.setWishList(WishListUtils.isWishList(shopProductViewModelOld.getId(), productWishList));
            shopProductViewModelOld.setShowWishList(showWishlist);
        }
    }

    public void mergeShopProductViewModelWithProductCampaigns(List<ShopProductViewModelOld> shopProductViewModelOldList, List<ShopProductCampaign> shopProductCampaignList) {
        for (ShopProductViewModelOld shopProductViewModelOld : shopProductViewModelOldList) {
            for (ShopProductCampaign shopProductCampaign : shopProductCampaignList) {
                if (shopProductViewModelOld.getId().equalsIgnoreCase(shopProductCampaign.getProductId())) {
                    shopProductViewModelOld.setDisplayedPrice(shopProductCampaign.getDiscountedPriceIdr());
                    shopProductViewModelOld.setOriginalPrice(shopProductCampaign.getOriginalPriceIdr());
                    shopProductViewModelOld.setDiscountPercentage(shopProductCampaign.getPercentageAmount());
                }
            }
        }
    }


    public List<ShopProductViewModelOld> convertFromProductFeatured(List<GMFeaturedProduct> gmFeaturedProductList) {
        List<ShopProductViewModelOld> shopProductViewModelOldList = new ArrayList<>();
        for (GMFeaturedProduct shopProduct : gmFeaturedProductList) {
            ShopProductLimitedFeaturedViewModelOld shopProductViewModel = convertFromProductFeatured(shopProduct);
            shopProductViewModelOldList.add(shopProductViewModel);
        }
        return shopProductViewModelOldList;
    }

    private ShopProductLimitedFeaturedViewModelOld convertFromProductFeatured(GMFeaturedProduct gmFeaturedProduct) {
        ShopProductLimitedFeaturedViewModelOld shopProductViewModel = new ShopProductLimitedFeaturedViewModelOld();

        shopProductViewModel.setId(gmFeaturedProduct.getProductId());
        shopProductViewModel.setName(gmFeaturedProduct.getName());
        shopProductViewModel.setDisplayedPrice(gmFeaturedProduct.getPrice());
        shopProductViewModel.setImageUrl(gmFeaturedProduct.getImageUri());
        shopProductViewModel.setProductUrl(gmFeaturedProduct.getUri());

        shopProductViewModel.setTotalReview(gmFeaturedProduct.getTotalReview());
        shopProductViewModel.setRating(gmFeaturedProduct.getRating());
        if (gmFeaturedProduct.getCashbackDetail() != null) {
            shopProductViewModel.setCashback(gmFeaturedProduct.getCashbackDetail().getCashbackPercent());
        }
        shopProductViewModel.setWholesale(gmFeaturedProduct.isWholesale());
        shopProductViewModel.setPo(gmFeaturedProduct.isPreorder());
        shopProductViewModel.setFreeReturn(gmFeaturedProduct.isReturnable());
        return shopProductViewModel;
    }

    public PagingList<ShopProductHomeViewModelOld> convertFromProductViewModel(PagingList<ShopProductViewModelOld> shopProductViewModelPagingList, int page, int perPage) {
        PagingList<ShopProductHomeViewModelOld> shopProductHomeViewModelPagingList = new PagingList<>();
        shopProductHomeViewModelPagingList.setPaging(shopProductViewModelPagingList.getPaging());
        shopProductHomeViewModelPagingList.setTotalData(shopProductViewModelPagingList.getTotalData());
        List<ShopProductHomeViewModelOld> shopProductHomeViewModels = new ArrayList<>();
        for(int i = 0; i<shopProductViewModelPagingList.getList().size(); i++){
            ShopProductViewModelOld shopProductViewModelOld =  shopProductViewModelPagingList.getList().get(i);
            ShopProductHomeViewModelOld shopProductHomeViewModel = convertFromProductModel(shopProductViewModelOld);
            shopProductHomeViewModel.setPositionTracking(getCurrentPageView(page, i + 1, perPage));
            shopProductHomeViewModels.add(shopProductHomeViewModel);
        }
        shopProductHomeViewModelPagingList.setList(shopProductHomeViewModels);
        return shopProductHomeViewModelPagingList;
    }

    private int getCurrentPageView(int currentPage, int position, int perPage) {
        if (currentPage > 1) {
            return (perPage * (currentPage -1)) + position;
        } else {
            return position;
        }
    }

    private ShopProductHomeViewModelOld convertFromProductModel(ShopProductViewModelOld shopProductViewModelOld) {
        ShopProductHomeViewModelOld shopProductHomeViewModel = new ShopProductHomeViewModelOld();
        shopProductHomeViewModel.setCashback(shopProductViewModelOld.getCashback());
        shopProductHomeViewModel.setDiscountPercentage(shopProductViewModelOld.getDiscountPercentage());
        shopProductHomeViewModel.setDisplayedPrice(shopProductViewModelOld.getDisplayedPrice());
        shopProductHomeViewModel.setFreeReturn(shopProductViewModelOld.isFreeReturn());
        shopProductHomeViewModel.setId(shopProductViewModelOld.getId());
        shopProductHomeViewModel.setImageUrl(shopProductViewModelOld.getImageUrl());
        shopProductHomeViewModel.setImageUrl300(shopProductViewModelOld.getImageUrl300());
        shopProductHomeViewModel.setImageUrl700(shopProductViewModelOld.getImageUrl700());
        shopProductHomeViewModel.setName(shopProductViewModelOld.getName());
        shopProductHomeViewModel.setOriginalPrice(shopProductViewModelOld.getOriginalPrice());
        shopProductHomeViewModel.setPo(shopProductViewModelOld.isPo());
        shopProductHomeViewModel.setProductUrl(shopProductViewModelOld.getProductUrl());
        shopProductHomeViewModel.setRating(shopProductViewModelOld.getRating());
        shopProductHomeViewModel.setShowWishList(shopProductViewModelOld.isShowWishList());
        shopProductHomeViewModel.setTotalReview(shopProductViewModelOld.getTotalReview());
        shopProductHomeViewModel.setWholesale(shopProductViewModelOld.isWholesale());
        shopProductHomeViewModel.setWishList(shopProductViewModelOld.isWishList());
        shopProductHomeViewModel.setSoldOut(shopProductViewModelOld.isSoldOut());
        return shopProductHomeViewModel;
    }

    private ShopProductLimitedFeaturedViewModelOld convertFromProductModelFeatured(ShopProductViewModelOld shopProductViewModelOld) {
        ShopProductLimitedFeaturedViewModelOld shopProductLimitedFeaturedViewModel = new ShopProductLimitedFeaturedViewModelOld();
        shopProductLimitedFeaturedViewModel.setCashback(shopProductViewModelOld.getCashback());
        shopProductLimitedFeaturedViewModel.setDiscountPercentage(shopProductViewModelOld.getDiscountPercentage());
        shopProductLimitedFeaturedViewModel.setDisplayedPrice(shopProductViewModelOld.getDisplayedPrice());
        shopProductLimitedFeaturedViewModel.setFreeReturn(shopProductViewModelOld.isFreeReturn());
        shopProductLimitedFeaturedViewModel.setId(shopProductViewModelOld.getId());
        shopProductLimitedFeaturedViewModel.setImageUrl(shopProductViewModelOld.getImageUrl());
        shopProductLimitedFeaturedViewModel.setImageUrl300(shopProductViewModelOld.getImageUrl300());
        shopProductLimitedFeaturedViewModel.setImageUrl700(shopProductViewModelOld.getImageUrl700());
        shopProductLimitedFeaturedViewModel.setName(shopProductViewModelOld.getName());
        shopProductLimitedFeaturedViewModel.setOriginalPrice(shopProductViewModelOld.getOriginalPrice());
        shopProductLimitedFeaturedViewModel.setPo(shopProductViewModelOld.isPo());
        shopProductLimitedFeaturedViewModel.setProductUrl(shopProductViewModelOld.getProductUrl());
        shopProductLimitedFeaturedViewModel.setRating(shopProductViewModelOld.getRating());
        shopProductLimitedFeaturedViewModel.setShowWishList(shopProductViewModelOld.isShowWishList());
        shopProductLimitedFeaturedViewModel.setTotalReview(shopProductViewModelOld.getTotalReview());
        shopProductLimitedFeaturedViewModel.setWholesale(shopProductViewModelOld.isWholesale());
        shopProductLimitedFeaturedViewModel.setWishList(shopProductViewModelOld.isWishList());
        shopProductLimitedFeaturedViewModel.setSoldOut(shopProductViewModelOld.isSoldOut());
        return shopProductLimitedFeaturedViewModel;
    }

    public List<ShopProductLimitedFeaturedViewModelOld> convertFromProductViewModelFeatured(List<ShopProductViewModelOld> shopProductViewModelOlds) {
        List<ShopProductLimitedFeaturedViewModelOld> shopProductLimitedFeaturedViewModels = new ArrayList<>();
        for(int i = 0; i < shopProductViewModelOlds.size(); i++){
            ShopProductViewModelOld shopProductViewModelOld =  shopProductViewModelOlds.get(i);
            ShopProductLimitedFeaturedViewModelOld shopProductLimitedFeaturedViewModel = convertFromProductModelFeatured(shopProductViewModelOld);
            shopProductLimitedFeaturedViewModel.setPositionTracking(i + 1);
            shopProductLimitedFeaturedViewModels.add(shopProductLimitedFeaturedViewModel);
        }
        return shopProductLimitedFeaturedViewModels;
    }
}
