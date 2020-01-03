/*
 * Created By Kulomady on 11/25/16 11:12 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:12 PM
 */

package com.tokopedia.core.network.entity.discovery;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.interfaces.Convert;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

/**
 * Created by noiz354 on 3/17/16.
 */

@Deprecated
public class BrowseShopModel {
    @SerializedName("result")
    public Result result;

    public static class Result {

        @SerializedName("q")
        public String query;

        @SerializedName("shops")
        public Shops[] shops;

        @SerializedName("paging")
        public PagingHandler.PagingHandlerModel paging;
    }

    public static class Shops extends Convert.DefaultConvert<Shops, ShopModel> {
        @SerializedName("shop_gold_shop")
        public String shopGoldShop;

        @SerializedName("shop_lucky")
        public String shopLucky;

        @SerializedName("shop_id")
        public String shopId;

        @SerializedName("reputation_score")
        public String reputationScore;

        @SerializedName("shop_domain")
        public String shopDomain;

        @SerializedName("shop_image")
        public String shopImage;

        @SerializedName("shop_total_favorite")
        public String shopTotalFavorite;

        @SerializedName("shop_total_transaction")
        public String shopTotalTransaction;

        @SerializedName("shop_name")
        public String shopName;

        @SerializedName("shop_location")
        public String shopLocation;

        @SerializedName("reputation_image_uri")
        public String reputationImageUri;

        @SerializedName("shop_url")
        public String shopUrl;

        @SerializedName("is_official")
        public boolean isOfficial;

        @SerializedName("product_images")
        public List<String> productImages;

        public boolean isFavorited;

        @Override
        public ShopModel from(Shops data) {
            return new ShopModel(data);
        }
    }
}


