package com.tokopedia.search.result.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchShopModel {

    @SerializedName("source")
    @Expose
    public String source = "";

    @SerializedName("search_url")
    @Expose
    public String searchUrl = "";

    @SerializedName("paging")
    @Expose
    public Paging paging = new Paging();

    @SerializedName("has_catalog")
    @Expose
    public String hasCatalog = "";

    @SerializedName("st")
    @Expose
    public String st = "";

    @SerializedName("q")
    @Expose
    public String query = "";

    @SerializedName("shops")
    @Expose
    public List<ShopItem> shopItemList = new ArrayList<>();

    public static class Paging {

        @SerializedName("uri_next")
        @Expose
        public String uriNext = "";

        @SerializedName("uri_previous")
        @Expose
        public String uriPrevious = "";
    }

    public static class ShopItem {

        @SerializedName("shop_id")
        @Expose
        public String shopId = "";

        @SerializedName("shop_name")
        @Expose
        public String shopName = "";

        @SerializedName("shop_domain")
        @Expose
        public String shopDomain = "";

        @SerializedName("shop_url")
        @Expose
        public String shopUrl = "";

        @SerializedName("shop_is_img")
        @Expose
        public String shopIsImg = "";

        @SerializedName("shop_image")
        @Expose
        public String shopImage = "";

        @SerializedName("shop_image_300")
        @Expose
        public String shopImage300 = "";

        @SerializedName("shop_description")
        @Expose
        public String shopDescription = "";

        @SerializedName("shop_tag_line")
        @Expose
        public String shopTagLine = "";

        @SerializedName("shop_location")
        @Expose
        public String shopLocation = "";

        @SerializedName("shop_total_transaction")
        @Expose
        public String shopTotalTransaction = "";

        @SerializedName("shop_total_favorite")
        @Expose
        public String shopTotalFavorite = "";

        @SerializedName("shop_gold_shop")
        @Expose
        public String shopGoldShop = "";

        @SerializedName("shop_is_owner")
        @Expose
        public String shopIsOwner = "";

        @SerializedName("shop_rate_speed")
        @Expose
        public String shopRateSpeed = "";

        @SerializedName("shop_rate_accuracy")
        @Expose
        public String shopRateAccuracy = "";

        @SerializedName("shop_rate_service")
        @Expose
        public String shopRateService = "";

        @SerializedName("shop_status")
        @Expose
        public String shopStatus = "";

        @SerializedName("product_images")
        @Expose
        public List<String> productImages = new ArrayList<>();

        @SerializedName("shop_lucky")
        @Expose
        public String shopLucky = "";

        @SerializedName("reputation_image_uri")
        @Expose
        public String reputationImageUri = "";

        @SerializedName("reputation_score")
        @Expose
        public String reputationScore = "";

        @SerializedName("is_official")
        @Expose
        public boolean isOfficial = false;

        @SerializedName("ga_key")
        @Expose
        public String gaKey = "";

        public boolean isFavorited = false;
    }
}
