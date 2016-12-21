
package com.tokopedia.core.shopinfo.models.productmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("shop_lucky")
    @Expose
    public int shopLucky;
    @SerializedName("shop_gold_status")
    @Expose
    public int shopGoldStatus;
    @SerializedName("shop_id")
    @Expose
    public int shopId;
    @SerializedName("product_rating_point")
    @Expose
    public String productRatingPoint;
    @SerializedName("product_department_id")
    @Expose
    public String productDepartmentId;
    @SerializedName("product_etalase")
    @Expose
    public String productEtalase;
    @SerializedName("shop_url")
    @Expose
    public String shopUrl;
    @SerializedName("shop_featured_shop")
    @Expose
    public int shopFeaturedShop;
    @SerializedName("product_status")
    @Expose
    public String productStatus;
    @SerializedName("product_id")
    @Expose
    public int productId;
    @SerializedName("product_image_full")
    @Expose
    public String productImageFull;
    @SerializedName("product_currency_id")
    @Expose
    public String productCurrencyId;
    @SerializedName("product_rating_desc")
    @Expose
    public String productRatingDesc;
    @SerializedName("product_currency")
    @Expose
    public String productCurrency;
    @SerializedName("product_talk_count")
    @Expose
    public String productTalkCount;
    @SerializedName("product_price_no_idr")
    @Expose
    public String productPriceNoIdr;
    @SerializedName("product_image")
    @Expose
    public String productImage;
    @SerializedName("product_price")
    @Expose
    public String productPrice;
    @SerializedName("product_sold_count")
    @Expose
    public String productSoldCount;
    @SerializedName("product_returnable")
    @Expose
    public int productReturnable;
    @SerializedName("shop_location")
    @Expose
    public int shopLocation;
    @SerializedName("product_normal_price")
    @Expose
    public int productNormalPrice;
    @SerializedName("product_image_300")
    @Expose
    public String productImage300;
    @SerializedName("shop_name")
    @Expose
    public int shopName;
    @SerializedName("product_review_count")
    @Expose
    public String productReviewCount;
    @SerializedName("shop_is_owner")
    @Expose
    public int shopIsOwner;
    @SerializedName("product_url")
    @Expose
    public String productUrl;
    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("product_preorder")
    @Expose
    public String productPreorder;
    @SerializedName("product_wholesale")
    @Expose
    public String productWholesale;
    @SerializedName("badges")
    @Expose
    public java.util.List<com.tokopedia.core.var.Badge> badges;
    @SerializedName("labels")
    @Expose
    public java.util.List<Label> labels;

}
