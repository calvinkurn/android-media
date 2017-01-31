package com.tokopedia.tkpd.home.feed.data.source.cloud.entity;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 3/4/16.
 */
@SuppressWarnings("unused")
public class FeedResponse {
    @SerializedName("data")
    private Result data;

    @SerializedName("status")
    private String status;

    @SerializedName("config")
    private Config config;

    @SerializedName("server_process_time")
    private String serverProcessTime;

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public static class Result {
        @SerializedName("locations")
        private String locations;

        @SerializedName("share_url")
        private String shareUrl;

        @SerializedName("department_id")
        private String departmentId;

        @SerializedName("hashtag")
        private String hashtag;

        @SerializedName("has_catalog")
        private String hasCatalog;

        @SerializedName("search_url")
        private String searchUrl;

        @SerializedName("st")
        private String st;

        @SerializedName("products")
        private Products[] products;

        @SerializedName("paging")
        private Paging paging;

        @SerializedName("breadcrumb")
        private String breadcrumb;

        public String getLocations() {
            return locations;
        }

        public void setLocations(String locations) {
            this.locations = locations;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getHashtag() {
            return hashtag;
        }

        public void setHashtag(String hashtag) {
            this.hashtag = hashtag;
        }

        public String getHasCatalog() {
            return hasCatalog;
        }

        public void setHasCatalog(String hasCatalog) {
            this.hasCatalog = hasCatalog;
        }

        public String getSearchUrl() {
            return searchUrl;
        }

        public void setSearchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
        }

        public String getSt() {
            return st;
        }

        public void setSt(String st) {
            this.st = st;
        }

        public Products[] getProducts() {
            return products;
        }

        public void setProducts(Products[] products) {
            this.products = products;
        }

        public Paging getPaging() {
            return paging;
        }

        public void setPaging(Paging paging) {
            this.paging = paging;
        }

        public String getBreadcrumb() {
            return breadcrumb;
        }

        public void setBreadcrumb(String breadcrumb) {
            this.breadcrumb = breadcrumb;
        }
    }

    public static class Products {
        @SerializedName("shop_lucky")
        private String shopLucky;

        @SerializedName("product_id")
        private String productId;

        @SerializedName("shop_id")
        private String shopId;

        @SerializedName("condition")
        private String condition;

        @SerializedName("product_price")
        private String productPrice;

        @SerializedName("product_review_count")
        private String productReviewCount;

        @SerializedName("product_sold_count")
        private String productSoldCount;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("product_wholesale")
        private String productWholesale;

        @SerializedName("product_url")
        private String productUrl;

        @SerializedName("shop_name")
        private String shopName;

        @SerializedName("product_talk_count")
        private String productTalkCount;

        @SerializedName("shop_location")
        private String shopLocation;

        @SerializedName("is_owner")
        private String isOwner;

        @SerializedName("rate")
        private String rate;

        @SerializedName("product_image")
        private String productImage;

        @SerializedName("product_image_full")
        private String productImageFull;

        @SerializedName("shop_url")
        private String shopUrl;

        @SerializedName("shop_gold_status")
        private String shopGoldStatus;

        @SerializedName("preorder")
        private String preorder;

        @SerializedName("labels")
        private List<Label> labels;

        @SerializedName("badges")
        private List<Badge> badges;

        String getShopLucky() {
            return shopLucky;
        }

        public void setShopLucky(String shopLucky) {
            this.shopLucky = shopLucky;
        }

        String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductReviewCount() {
            return productReviewCount;
        }

        public void setProductReviewCount(String productReviewCount) {
            this.productReviewCount = productReviewCount;
        }

        public String getProductSoldCount() {
            return productSoldCount;
        }

        public void setProductSoldCount(String productSoldCount) {
            this.productSoldCount = productSoldCount;
        }

        String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductWholesale() {
            return productWholesale;
        }

        public void setProductWholesale(String productWholesale) {
            this.productWholesale = productWholesale;
        }

        public String getProductUrl() {
            return productUrl;
        }

        public void setProductUrl(String productUrl) {
            this.productUrl = productUrl;
        }

        String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getProductTalkCount() {
            return productTalkCount;
        }

        public void setProductTalkCount(String productTalkCount) {
            this.productTalkCount = productTalkCount;
        }

        public String getShopLocation() {
            return shopLocation;
        }

        public void setShopLocation(String shopLocation) {
            this.shopLocation = shopLocation;
        }

        public String getIsOwner() {
            return isOwner;
        }

        public void setIsOwner(String isOwner) {
            this.isOwner = isOwner;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductImageFull() {
            return productImageFull;
        }

        public void setProductImageFull(String productImageFull) {
            this.productImageFull = productImageFull;
        }

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        String getShopGoldStatus() {
            return shopGoldStatus;
        }

        public void setShopGoldStatus(String shopGoldStatus) {
            this.shopGoldStatus = shopGoldStatus;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }

        List<Label> getLabels() {
            return labels;
        }

        public void setLabels(List<Label> labels) {
            this.labels = labels;
        }
    }

    public static class Paging {
        @SerializedName("uri_previous")
        private String uriPrevious;

        @SerializedName("uri_next")
        private String uriNext;

        public String getUriPrevious() {
            return uriPrevious;
        }

        public void setUriPrevious(String uriPrevious) {
            this.uriPrevious = uriPrevious;
        }

        public String getUriNext() {
            return uriNext;
        }

        public void setUriNext(String uriNext) {
            this.uriNext = uriNext;
        }

        String getUrlParam(String key) {
            Uri uri = Uri.parse(uriNext);
            return uri.getQueryParameter(key);
        }

        public int getStartIndex() {
            if (uriNext != null && !uriNext.equals(""))
                return Integer.parseInt(getUrlParam("start"));
            else
                return -1;
        }
    }

    public static class Config {
        @SerializedName("backoff_multi")
        private String backoffMulti;

        @SerializedName("timeout")
        private String timeout;

        @SerializedName("max_retries")
        private String maxRetries;
    }

     static ProductItem toProductItem(Products product) {
        ProductItem productItem = new ProductItem();
        productItem.setId(product.getProductId());
        productItem.setImgUri(product.getProductImage());
        productItem.setIsNewGold(Integer.parseInt(product.getShopGoldStatus()));
        productItem.setName(product.getProductName());
        productItem.setLuckyShop(product.getShopLucky());
        productItem.setPrice(product.getProductPrice());
        productItem.setShop(product.getShopName());
        productItem.setShopId(Integer.parseInt(product.getShopId()));
        productItem.setBadges(product.getBadges());
        productItem.setLabels(product.getLabels());
        productItem.preorder = product.preorder;
        productItem.wholesale = product.productWholesale;
        productItem.shop_location = product.shopLocation;
        return productItem;
    }

    public static ArrayList<ProductItem> toProductItems(Products... productFeed2List) {
        ArrayList<ProductItem> result = new ArrayList<>();
        for (Products productFeed2 : productFeed2List) {
            result.add(toProductItem(productFeed2));
        }
        return result;
    }
}
