package com.tokopedia.shop.product.view.model;

import com.tokopedia.abstraction.common.utils.network.TextApiUtils;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductBadge;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductLabel;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;

import java.util.List;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductViewModel implements BaseShopProductViewModel {

    private static final String BADGE_FREE_RETURN = "Free Return";
    private static final String LABEL_CASHBACK = "Cashback";
    private static final String LABEL_PERCENTAGE = "%";

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    private String id;
    private String name;
    private String displayedPrice;
    private String originalPrice;
    private String discountPercentage;
    private String imageUrl;
    private String imageUrl300;
    private String imageUrl700;
    private String totalReview;
    private double rating;
    private double cashback;
    private boolean wholesale;
    private boolean po;
    private boolean freeReturn;
    private boolean wishList;
    private String productUrl;
    private boolean showWishList;
    private boolean isSoldOut;

    public ShopProductViewModel() {
    }

    public ShopProductViewModel (ShopProduct shopProduct) {
        setId(shopProduct.getProductId());
        setName(shopProduct.getProductName());
        setDisplayedPrice(shopProduct.getProductPrice());
        setImageUrl(shopProduct.getProductImage());
        setImageUrl300(shopProduct.getProductImage300());
        setImageUrl700(shopProduct.getProductImage700());
        setProductUrl(shopProduct.getProductUrl());
        setRating(shopProduct.getRating());
        setPo(TextApiUtils.isValueTrue(shopProduct.getProductPreorder()));
        setTotalReview(shopProduct.getProductReviewCount());
        setWholesale(TextApiUtils.isValueTrue(shopProduct.getProductWholesale()));
        if (shopProduct.getBadges() != null && shopProduct.getBadges().size() > 0) {
            for (ShopProductBadge badge : shopProduct.getBadges()) {
                if (BADGE_FREE_RETURN.equalsIgnoreCase(badge.getTitle())) {
                    setFreeReturn(true);
                    break;
                }
            }
        }
        List<ShopProductLabel> shopProductLabelList = shopProduct.getLabels();
        if (shopProductLabelList != null) {
            for (ShopProductLabel shopProductLabel : shopProductLabelList) {
                if (shopProductLabel.getTitle().startsWith(LABEL_CASHBACK)) {
                    String cashbackText = shopProductLabel.getTitle();
                    cashbackText = cashbackText.replace(LABEL_CASHBACK, "");
                    cashbackText = cashbackText.replace(LABEL_PERCENTAGE, "");
                    double cashbackPercentage = Double.parseDouble(cashbackText.trim());
                    setCashback(cashbackPercentage);
                    break;
                }
            }
        }
        setSoldOut(shopProduct.isSoldOutStatus());
    }

    public ShopProductViewModel(GMFeaturedProduct gmFeaturedProduct) {
        setId(gmFeaturedProduct.getProductId());
        setName(gmFeaturedProduct.getName());
        setDisplayedPrice(gmFeaturedProduct.getPrice());
        setImageUrl(gmFeaturedProduct.getImageUri());
        setProductUrl(gmFeaturedProduct.getUri());

        setTotalReview(gmFeaturedProduct.getTotalReview());
        setRating(gmFeaturedProduct.getRating());
        if (gmFeaturedProduct.getCashbackDetail() != null) {
            setCashback(gmFeaturedProduct.getCashbackDetail().getCashbackPercent());
        }
        setWholesale(gmFeaturedProduct.isWholesale());
        setPo(gmFeaturedProduct.isPreorder());
        setFreeReturn(gmFeaturedProduct.isReturnable());
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayedPrice() {
        return displayedPrice;
    }

    public void setDisplayedPrice(String displayedPrice) {
        this.displayedPrice = displayedPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl300() {
        return imageUrl300;
    }

    public void setImageUrl300(String imageUrl300) {
        this.imageUrl300 = imageUrl300;
    }

    public String getImageUrl700() {
        return imageUrl700;
    }

    public void setImageUrl700(String imageUrl700) {
        this.imageUrl700 = imageUrl700;
    }

    public String getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(String totalReview) {
        this.totalReview = totalReview;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getCashback() {
        return cashback;
    }

    public void setCashback(double cashback) {
        this.cashback = cashback;
    }

    public boolean isWholesale() {
        return wholesale;
    }

    public void setWholesale(boolean wholesale) {
        this.wholesale = wholesale;
    }

    public boolean isPo() {
        return po;
    }

    public void setPo(boolean po) {
        this.po = po;
    }

    public boolean isFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        this.freeReturn = freeReturn;
    }

    public boolean isWishList() {
        return wishList;
    }

    public void setWishList(boolean wishList) {
        this.wishList = wishList;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public boolean isShowWishList() {
        return showWishList;
    }

    public void setShowWishList(boolean showWishList) {
        this.showWishList = showWishList;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }


}
