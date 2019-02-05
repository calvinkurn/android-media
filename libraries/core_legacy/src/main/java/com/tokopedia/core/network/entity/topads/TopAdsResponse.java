/*
 * Created By Kulomady on 11/25/16 11:07 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:07 PM
 */

package com.tokopedia.core.network.entity.topads;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian & radityagumay on 3/4/16.
 */
public class TopAdsResponse {

    @SerializedName("data")
    public List<Data> data;

    public TopAdsResponse() {
        this.data = new ArrayList<>();
    }

    public static class Data {

        @SerializedName("id")
        public String id;
        @SerializedName("ad_ref_key")
        public String adRefKey;
        @SerializedName("redirect")
        public String redirect;
        @SerializedName("sticker_id")
        public String stickerId;
        @SerializedName("sticker_image")
        public String stickerImage;
        @SerializedName("product_click_url")
        public String productClickUrl;
        @SerializedName("shop_click_url")
        public String shopClickUrl;
        @SerializedName("product")
        public Product product;
        @SerializedName("shop")
        public Shop shop;

        public static class Shop {

            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("location")
            public String location;
            @SerializedName("gold_shop")
            public boolean goldShop;
            @SerializedName("lucky_shop")
            public String luckyShop;
            @SerializedName("uri")
            public String uri;
            @SerializedName("is_owner")
            public boolean isOwner;
            @SerializedName("badges")
            public List<Badge> badges;
        }

        public static class Product {
            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("image")
            public Image image;
            @SerializedName("uri")
            public String uri;
            @SerializedName("price_format")
            public String priceFormat;
            @SerializedName("wholesale_price")
            public List<WholesalePrice> wholesalePrice = new ArrayList<WholesalePrice>();
            @SerializedName("count_talk_format")
            public String countTalkFormat;
            @SerializedName("count_review_format")
            public String countReviewFormat;
            @SerializedName("category")
            public Category category;
            @SerializedName("product_preorder")
            public boolean preorder;
            @SerializedName("product_wholesale")
            public boolean wholesale;
            @SerializedName("labels")
            public List<Label> labels;

            public static class WholesalePrice {
                @SerializedName("quantity_min_format")
                public String quantityMinFormat;
                @SerializedName("quantity_max_format")
                public String quantityMaxFormat;
                @SerializedName("price_format")
                public String priceFormat;
            }

            public static class Category {
                @SerializedName("id")
                public String id;
            }

            public static class Image {
                @SerializedName("m_url")
                public String mUrl;
                @SerializedName("s_url")
                public String sUrl;
                @SerializedName("xs_url")
                public String xsUrl;
            }
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static final class TopAdsContainer implements ObjContainer<TopAdsResponse> {

        TopAdsResponse topAdsResponse;
        public int page;

        public TopAdsContainer(TopAdsResponse topAdsResponse) {
            this.topAdsResponse = topAdsResponse;
        }

        @Override
        public TopAdsResponse body() {
            return topAdsResponse;
        }
    }
}

