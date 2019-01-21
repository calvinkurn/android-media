
package com.tokopedia.core.shopinfo.models.productmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.*;
import com.tokopedia.core.var.Badge;

import java.util.ArrayList;

public class List implements Parcelable {

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
    public String shopLocation;
    @SerializedName("product_normal_price")
    @Expose
    public int productNormalPrice;
    @SerializedName("product_image_300")
    @Expose
    public String productImage300;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
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

    @SerializedName("product_price_unformatted")
    @Expose
    public double productPriceUnformatted;

    public ShopProductCampaign shopProductCampaign;

    public List() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shopLucky);
        dest.writeInt(this.shopGoldStatus);
        dest.writeInt(this.shopId);
        dest.writeString(this.productRatingPoint);
        dest.writeString(this.productDepartmentId);
        dest.writeString(this.productEtalase);
        dest.writeString(this.shopUrl);
        dest.writeInt(this.shopFeaturedShop);
        dest.writeString(this.productStatus);
        dest.writeInt(this.productId);
        dest.writeString(this.productImageFull);
        dest.writeString(this.productCurrencyId);
        dest.writeString(this.productRatingDesc);
        dest.writeString(this.productCurrency);
        dest.writeString(this.productTalkCount);
        dest.writeString(this.productPriceNoIdr);
        dest.writeString(this.productImage);
        dest.writeString(this.productPrice);
        dest.writeString(this.productSoldCount);
        dest.writeInt(this.productReturnable);
        dest.writeString(this.shopLocation);
        dest.writeInt(this.productNormalPrice);
        dest.writeString(this.productImage300);
        dest.writeString(this.shopName);
        dest.writeString(this.productReviewCount);
        dest.writeInt(this.shopIsOwner);
        dest.writeString(this.productUrl);
        dest.writeString(this.productName);
        dest.writeString(this.productPreorder);
        dest.writeString(this.productWholesale);
        dest.writeTypedList(this.badges);
        dest.writeTypedList(this.labels);
        dest.writeParcelable(this.shopProductCampaign, flags);
    }

    protected List(Parcel in) {
        this.shopLucky = in.readInt();
        this.shopGoldStatus = in.readInt();
        this.shopId = in.readInt();
        this.productRatingPoint = in.readString();
        this.productDepartmentId = in.readString();
        this.productEtalase = in.readString();
        this.shopUrl = in.readString();
        this.shopFeaturedShop = in.readInt();
        this.productStatus = in.readString();
        this.productId = in.readInt();
        this.productImageFull = in.readString();
        this.productCurrencyId = in.readString();
        this.productRatingDesc = in.readString();
        this.productCurrency = in.readString();
        this.productTalkCount = in.readString();
        this.productPriceNoIdr = in.readString();
        this.productImage = in.readString();
        this.productPrice = in.readString();
        this.productSoldCount = in.readString();
        this.productReturnable = in.readInt();
        this.shopLocation = in.readString();
        this.productNormalPrice = in.readInt();
        this.productImage300 = in.readString();
        this.shopName = in.readString();
        this.productReviewCount = in.readString();
        this.shopIsOwner = in.readInt();
        this.productUrl = in.readString();
        this.productName = in.readString();
        this.productPreorder = in.readString();
        this.productWholesale = in.readString();
        this.badges = in.createTypedArrayList(Badge.CREATOR);
        this.labels = in.createTypedArrayList(Label.CREATOR);
        this.shopProductCampaign = in.readParcelable(ShopProductCampaign.class.getClassLoader());
    }

    public static final Creator<List> CREATOR = new Creator<List>() {
        @Override
        public List createFromParcel(Parcel source) {
            return new List(source);
        }

        @Override
        public List[] newArray(int size) {
            return new List[size];
        }
    };
}
