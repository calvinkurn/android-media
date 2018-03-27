package com.tokopedia.posapp.product.common.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetail {
    @SerializedName("shop_lucky")
    @Expose
    private  int shopLucky;
    @SerializedName("shop_gold_status")
    @Expose
    private int shopGoldStatus;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("product_rating_point")
    @Expose
    private String productRatingPoint;
    @SerializedName("product_department_id")
    @Expose
    private String productDepartmentId;
    @SerializedName("product_etalase")
    @Expose
    private String productEtalase;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_featured_shop")
    @Expose
    private int shopFeaturedShop;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("product_image_full")
    @Expose
    private String productImageFull;
    @SerializedName("product_currency_id")
    @Expose
    private String productCurrencyId;
    @SerializedName("product_rating_desc")
    @Expose
    private String productRatingDesc;
    @SerializedName("product_currency")
    @Expose
    private String productCurrency;
    @SerializedName("product_talk_count")
    @Expose
    private String productTalkCount;
    @SerializedName("product_price_no_idr")
    @Expose
    private String productPriceNoIdr;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_sold_count")
    @Expose
    private String productSoldCount;
    @SerializedName("product_returnable")
    @Expose
    private int productReturnable;
    @SerializedName("shop_location")
    @Expose
    private String shopLocation;
    @SerializedName("product_normal_price")
    @Expose
    private int productNormalPrice;
    @SerializedName("product_image_300")
    @Expose
    private String productImage300;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("product_review_count")
    @Expose
    private String productReviewCount;
    @SerializedName("shop_is_owner")
    @Expose
    private int shopIsOwner;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("product_preorder")
    @Expose
    public String productPreorder;
    @SerializedName("product_wholesale")
    @Expose
    private String productWholesale;
    @SerializedName("badges")
    @Expose
    private List<Badge> badges;
    @SerializedName("labels")
    @Expose
    private List<Label> labels;
    @SerializedName("product_price_unformatted")
    @Expose
    private double productPriceUnformatted;

    private ProductCampaign productCampaign;

    public int getShopLucky() {
        return shopLucky;
    }

    public void setShopLucky(int shopLucky) {
        this.shopLucky = shopLucky;
    }

    public int getShopGoldStatus() {
        return shopGoldStatus;
    }

    public void setShopGoldStatus(int shopGoldStatus) {
        this.shopGoldStatus = shopGoldStatus;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getProductRatingPoint() {
        return productRatingPoint;
    }

    public void setProductRatingPoint(String productRatingPoint) {
        this.productRatingPoint = productRatingPoint;
    }

    public String getProductDepartmentId() {
        return productDepartmentId;
    }

    public void setProductDepartmentId(String productDepartmentId) {
        this.productDepartmentId = productDepartmentId;
    }

    public String getProductEtalase() {
        return productEtalase;
    }

    public void setProductEtalase(String productEtalase) {
        this.productEtalase = productEtalase;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public int getShopFeaturedShop() {
        return shopFeaturedShop;
    }

    public void setShopFeaturedShop(int shopFeaturedShop) {
        this.shopFeaturedShop = shopFeaturedShop;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductImageFull() {
        return productImageFull;
    }

    public void setProductImageFull(String productImageFull) {
        this.productImageFull = productImageFull;
    }

    public String getProductCurrencyId() {
        return productCurrencyId;
    }

    public void setProductCurrencyId(String productCurrencyId) {
        this.productCurrencyId = productCurrencyId;
    }

    public String getProductRatingDesc() {
        return productRatingDesc;
    }

    public void setProductRatingDesc(String productRatingDesc) {
        this.productRatingDesc = productRatingDesc;
    }

    public String getProductCurrency() {
        return productCurrency;
    }

    public void setProductCurrency(String productCurrency) {
        this.productCurrency = productCurrency;
    }

    public String getProductTalkCount() {
        return productTalkCount;
    }

    public void setProductTalkCount(String productTalkCount) {
        this.productTalkCount = productTalkCount;
    }

    public String getProductPriceNoIdr() {
        return productPriceNoIdr;
    }

    public void setProductPriceNoIdr(String productPriceNoIdr) {
        this.productPriceNoIdr = productPriceNoIdr;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSoldCount() {
        return productSoldCount;
    }

    public void setProductSoldCount(String productSoldCount) {
        this.productSoldCount = productSoldCount;
    }

    public int getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(int productReturnable) {
        this.productReturnable = productReturnable;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public int getProductNormalPrice() {
        return productNormalPrice;
    }

    public void setProductNormalPrice(int productNormalPrice) {
        this.productNormalPrice = productNormalPrice;
    }

    public String getProductImage300() {
        return productImage300;
    }

    public void setProductImage300(String productImage300) {
        this.productImage300 = productImage300;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductReviewCount() {
        return productReviewCount;
    }

    public void setProductReviewCount(String productReviewCount) {
        this.productReviewCount = productReviewCount;
    }

    public int getShopIsOwner() {
        return shopIsOwner;
    }

    public void setShopIsOwner(int shopIsOwner) {
        this.shopIsOwner = shopIsOwner;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(String productPreorder) {
        this.productPreorder = productPreorder;
    }

    public String getProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(String productWholesale) {
        this.productWholesale = productWholesale;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public double getProductPriceUnformatted() {
        return productPriceUnformatted;
    }

    public void setProductPriceUnformatted(double productPriceUnformatted) {
        this.productPriceUnformatted = productPriceUnformatted;
    }

    public ProductCampaign getProductCampaign() {
        return productCampaign;
    }

    public void setProductCampaign(ProductCampaign productCampaign) {
        this.productCampaign = productCampaign;
    }
}
