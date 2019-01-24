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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 3/17/16.
 */
public class BrowseShopModel {
    @SerializedName("result")
    public Result result;

    @SerializedName("status")
    String status;

    @SerializedName("config")
    BrowseProductModel.Config config;

    @SerializedName("server_process_time")
    String serverProcessTime;

    public static class Result {
        @SerializedName("source")
        String source;

        @SerializedName("q")
        public String query;

        @SerializedName("shops")
        public Shops[] shops;

        @SerializedName("has_catalog")
        String hasCatalog;

        @SerializedName("search_url")
        String searchUrl;

        @SerializedName("st")
        String st;

        @SerializedName("paging")
        public PagingHandler.PagingHandlerModel paging;
    }

    public static class Shops extends Convert.DefaultConvert<Shops, ShopModel> {
        @SerializedName("shop_gold_shop")
        public String shopGoldShop;

        @SerializedName("shop_description")
        public String shopDescription;

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

        @SerializedName("shop_rate_accuracy")
        public String shopRateAccuracy;

        @SerializedName("shop_image_300")
        public String shopImage300;

        @SerializedName("shop_name")
        public String shopName;

        @SerializedName("shop_is_img")
        public String shopIsImg;

        @SerializedName("shop_location")
        public String shopLocation;

        @SerializedName("reputation_image_uri")
        public String reputationImageUri;

        @SerializedName("shop_rate_service")
        public String shopRateService;

        @SerializedName("shop_is_owner")
        public String shopIsOwner;

        @SerializedName("shop_url")
        public String shopUrl;

        @SerializedName("shop_rate_speed")
        public String shopRateSpeed;

        @SerializedName("shop_tag_line")
        public String shopTagLine;

        @SerializedName("shop_status")
        public String shopStatus;

        @SerializedName("is_official")
        public boolean isOfficial;

        @SerializedName("product_images")
        public List<String> productImages;

        public boolean isFavorited;

        @Override
        public ShopModel from(Shops data) {
            return new ShopModel(data);
        }

        public static List<ShopModel> toShopItemList(Shops... datas) {
            ArrayList<ShopModel> result = new ArrayList<>();
            for (Shops s :
                    datas) {
                result.add(s.from(s));
            }
            return result;
        }
    }


    public static final class BrowseShopContainer implements ObjContainer<BrowseShopModel> {
        BrowseShopModel browseShopModel;

        public BrowseShopContainer(BrowseShopModel browseShopModel) {
            this.browseShopModel = browseShopModel;
        }

        @Override
        public BrowseShopModel body() {
            return browseShopModel;
        }
    }
}


