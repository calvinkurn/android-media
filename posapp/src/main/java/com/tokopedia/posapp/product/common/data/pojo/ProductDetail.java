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
    private int productId;
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
}
