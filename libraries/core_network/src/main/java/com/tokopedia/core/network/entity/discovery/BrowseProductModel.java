/*
 * Created By Kulomady on 11/25/16 11:11 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:11 PM
 */

package com.tokopedia.core.network.entity.discovery;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.interfaces.Convert;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by noiz354 on 3/17/16.
 */

@Deprecated
public class BrowseProductModel {
    @SerializedName("data")
    public Result result;

    @SerializedName("status")
    public String status;

    @SerializedName("config")
    public Config config;

    @SerializedName("header")
    public Header header;

    @SerializedName("server_process_time")
    public String serverProcessTime;

    private Data categoryData;

    public Data getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(Data categoryData) {
        this.categoryData = categoryData;
    }

    public static class Result {
        @SerializedName("locations")
        public String locations;

        @SerializedName("share_url")
        public String shareUrl;

        @SerializedName("department_id")
        public String departmentId;

        @SerializedName("search_url")
        public String searchUrl;

        @SerializedName("st")
        public String st;

        @SerializedName("products")
        public Products[] products;

        @SerializedName("paging")
        public PagingHandler.PagingHandlerModel paging;

        @SerializedName("breadcrumb")
        public Breadcrumb[] breadcrumb;

        @SerializedName("redirect_url")
        public String redirect_url;

    }

    @Parcel
    public static class Products extends Convert.DefaultConvert<Products, ProductItem>{
        @SerializedName("shop_lucky")
        public String shopLucky;

        @SerializedName("product_id")
        public String productId;

        @SerializedName("shop_id")
        public String shopId;

        @SerializedName("condition")
        public String condition;

        @SerializedName("product_price")
        public String productPrice;

        @SerializedName("product_review_count")
        public String productReviewCount;

        @SerializedName("product_sold_count")
        public String productSoldCount;

        @SerializedName("product_name")
        public String productName;

        @SerializedName("product_wholesale")
        public String productWholesale;

        @SerializedName("product_url")
        public String productUrl;

        @SerializedName("shop_name")
        public String shopName;

        @SerializedName("product_talk_count")
        public String productTalkCount;

        @SerializedName("shop_location")
        public String shopLocation;

        @SerializedName("is_owner")
        public String isOwner;

        @SerializedName("rate")
        public String rate;

        @SerializedName("product_image")
        public String productImage;

        @SerializedName("product_image_full")
        public String productImageFull;

        @SerializedName("shop_url")
        public String shopUrl;

        @SerializedName("shop_gold_status")
        public String shopGoldStatus;

        @SerializedName("preorder")
        public String preOrder;

        @SerializedName("product_preorder")
        public String productPreOder;

        @SerializedName("labels")
        public List<Label> labels;

        @SerializedName("badges")
        public List<Badge> badges;

        @Override
        public ProductItem from(Products data) {
            ProductItem productItem = new ProductItem();
            productItem.setId(data.productId);
            productItem.setImgUri(data.productImage);
            productItem.setIsNewGold(Integer.parseInt(data.shopGoldStatus));
            productItem.setName(data.productName);
            productItem.setLuckyShop(data.shopLucky);
            productItem.setBadges(data.badges);
            productItem.setLabels(data.labels);
            productItem.setPrice(data.productPrice);
            productItem.setShop(data.shopName);
            productItem.setShopLocation(data.shopLocation);
            productItem.setShopId(Integer.parseInt(data.shopId));
            productItem.preorder = data.productPreOder;
            productItem.wholesale = data.productWholesale;
            productItem.setType(TkpdState.RecyclerView.VIEW_PRODUCT);
            productItem.setRating(data.rate);
            productItem.setReviewCount(data.productReviewCount);
            return productItem;
        }
    }

    public static class Config {

    }

    public static class Header {
        @SerializedName("total_data")
        Long totalData = 0L;

        public Long getTotalData() {
            return totalData;
        }

        public void setTotalData(Long totalData) {
            this.totalData = totalData;
        }
    }
}
