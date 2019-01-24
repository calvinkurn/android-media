package com.tokopedia.core.network.entity.home.recentView;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class RecentView extends RecyclerViewItem implements Parcelable {

    @SerializedName("badges")
    private List<Badge> mBadges;
    @SerializedName("labels")
    private List<Label> mLabels;
    @SerializedName("product_available")
    private Long mProductAvailable;
    @SerializedName("product_currency")
    private Long mProductCurrency;
    @SerializedName("product_currency_id")
    private Long mProductCurrencyId;
    @SerializedName("product_department_id")
    private Long mProductDepartmentId;
    @SerializedName("product_etalase")
    private Long mProductEtalase;
    @SerializedName("product_id")
    private Long mProductId;
    @SerializedName("product_image")
    private String mProductImage;
    @SerializedName("product_image_300")
    private Long mProductImage300;
    @SerializedName("product_image_full")
    private String mProductImageFull;
    @SerializedName("product_name")
    private String mProductName;
    @SerializedName("product_normal_price")
    private Long mProductNormalPrice;
    @SerializedName("product_preorder")
    private Long mProductPreorder;
    @SerializedName("product_price")
    private String mProductPrice;
    @SerializedName("product_price_no_idr")
    private Long mProductPriceNoIdr;
    @SerializedName("product_rating_desc")
    private Long mProductRatingDesc;
    @SerializedName("product_rating_point")
    private Long mProductRatingPoint;
    @SerializedName("product_returnable")
    private Long mProductReturnable;
    @SerializedName("product_review_count")
    private Long mProductReviewCount;
    @SerializedName("product_sold_count")
    private Long mProductSoldCount;
    @SerializedName("product_status")
    private Long mProductStatus;
    @SerializedName("product_talk_count")
    private Long mProductTalkCount;
    @SerializedName("product_url")
    private String mProductUrl;
    @SerializedName("product_wholesale")
    private Long mProductWholesale;
    @SerializedName("shop_featured_shop")
    private Long mShopFeaturedShop;
    @SerializedName("shop_gold_status")
    private Long mShopGoldStatus;
    @SerializedName("shop_id")
    private Long mShopId;
    @SerializedName("shop_is_owner")
    private Long mShopIsOwner;
    @SerializedName("shop_location")
    private String mShopLocation;
    @SerializedName("shop_lucky")
    private String mShopLucky;
    @SerializedName("shop_name")
    private String mShopName;
    @SerializedName("shop_url")
    private String mShopUrl;

    public List<Badge> getBadges() {
        return mBadges;
    }

    public void setBadges(List<Badge> badges) {
        mBadges = badges;
    }

    public List<Label> getLabels() {
        return mLabels;
    }

    public void setLabels(List<Label> labels) {
        mLabels = labels;
    }

    public Long getProductAvailable() {
        return mProductAvailable;
    }

    public void setProductAvailable(Long product_available) {
        mProductAvailable = product_available;
    }

    public Long getProductCurrency() {
        return mProductCurrency;
    }

    public void setProductCurrency(Long product_currency) {
        mProductCurrency = product_currency;
    }

    public Long getProductCurrencyId() {
        return mProductCurrencyId;
    }

    public void setProductCurrencyId(Long product_currency_id) {
        mProductCurrencyId = product_currency_id;
    }

    public Long getProductDepartmentId() {
        return mProductDepartmentId;
    }

    public void setProductDepartmentId(Long product_department_id) {
        mProductDepartmentId = product_department_id;
    }

    public Long getProductEtalase() {
        return mProductEtalase;
    }

    public void setProductEtalase(Long product_etalase) {
        mProductEtalase = product_etalase;
    }

    public Long getProductId() {
        return mProductId;
    }

    public void setProductId(Long product_id) {
        mProductId = product_id;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public void setProductImage(String product_image) {
        mProductImage = product_image;
    }

    public Long getProductImage300() {
        return mProductImage300;
    }

    public void setProductImage300(Long product_image_300) {
        mProductImage300 = product_image_300;
    }

    public String getProductImageFull() {
        return mProductImageFull;
    }

    public void setProductImageFull(String product_image_full) {
        mProductImageFull = product_image_full;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String product_name) {
        mProductName = product_name;
    }

    public Long getProductNormalPrice() {
        return mProductNormalPrice;
    }

    public void setProductNormalPrice(Long product_normal_price) {
        mProductNormalPrice = product_normal_price;
    }

    public Long getProductPreorder() {
        return mProductPreorder;
    }

    public void setProductPreorder(Long product_preorder) {
        mProductPreorder = product_preorder;
    }

    public String getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(String product_price) {
        mProductPrice = product_price;
    }

    public Long getProductPriceNoIdr() {
        return mProductPriceNoIdr;
    }

    public void setProductPriceNoIdr(Long product_price_no_idr) {
        mProductPriceNoIdr = product_price_no_idr;
    }

    public Long getProductRatingDesc() {
        return mProductRatingDesc;
    }

    public void setProductRatingDesc(Long product_rating_desc) {
        mProductRatingDesc = product_rating_desc;
    }

    public Long getProductRatingPoint() {
        return mProductRatingPoint;
    }

    public void setProductRatingPoint(Long product_rating_point) {
        mProductRatingPoint = product_rating_point;
    }

    public Long getProductReturnable() {
        return mProductReturnable;
    }

    public void setProductReturnable(Long product_returnable) {
        mProductReturnable = product_returnable;
    }

    public Long getProductReviewCount() {
        return mProductReviewCount;
    }

    public void setProductReviewCount(Long product_review_count) {
        mProductReviewCount = product_review_count;
    }

    public Long getProductSoldCount() {
        return mProductSoldCount;
    }

    public void setProductSoldCount(Long product_sold_count) {
        mProductSoldCount = product_sold_count;
    }

    public Long getProductStatus() {
        return mProductStatus;
    }

    public void setProductStatus(Long product_status) {
        mProductStatus = product_status;
    }

    public Long getProductTalkCount() {
        return mProductTalkCount;
    }

    public void setProductTalkCount(Long product_talk_count) {
        mProductTalkCount = product_talk_count;
    }

    public String getProductUrl() {
        return mProductUrl;
    }

    public void setProductUrl(String product_url) {
        mProductUrl = product_url;
    }

    public Long getProductWholesale() {
        return mProductWholesale;
    }

    public void setProductWholesale(Long product_wholesale) {
        mProductWholesale = product_wholesale;
    }

    public Long getShopFeaturedShop() {
        return mShopFeaturedShop;
    }

    public void setShopFeaturedShop(Long shop_featured_shop) {
        mShopFeaturedShop = shop_featured_shop;
    }

    public Long getShopGoldStatus() {
        return mShopGoldStatus;
    }

    public void setShopGoldStatus(Long shop_gold_status) {
        mShopGoldStatus = shop_gold_status;
    }

    public Long getShopId() {
        return mShopId;
    }

    public void setShopId(Long shop_id) {
        mShopId = shop_id;
    }

    public Long getShopIsOwner() {
        return mShopIsOwner;
    }

    public void setShopIsOwner(Long shop_is_owner) {
        mShopIsOwner = shop_is_owner;
    }

    public String getShopLocation() {
        return mShopLocation;
    }

    public void setShopLocation(String shop_location) {
        mShopLocation = shop_location;
    }

    public String getShopLucky() {
        return mShopLucky;
    }

    public void setShopLucky(String shop_lucky) {
        mShopLucky = shop_lucky;
    }

    public String getShopName() {
        return mShopName;
    }

    public void setShopName(String shop_name) {
        mShopName = shop_name;
    }

    public String getShopUrl() {
        return mShopUrl;
    }

    public void setShopUrl(String shop_url) {
        mShopUrl = shop_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.mBadges);
        dest.writeList(this.mLabels);
        dest.writeValue(this.mProductAvailable);
        dest.writeValue(this.mProductCurrency);
        dest.writeValue(this.mProductCurrencyId);
        dest.writeValue(this.mProductDepartmentId);
        dest.writeValue(this.mProductEtalase);
        dest.writeValue(this.mProductId);
        dest.writeString(this.mProductImage);
        dest.writeValue(this.mProductImage300);
        dest.writeString(this.mProductImageFull);
        dest.writeString(this.mProductName);
        dest.writeValue(this.mProductNormalPrice);
        dest.writeValue(this.mProductPreorder);
        dest.writeString(this.mProductPrice);
        dest.writeValue(this.mProductPriceNoIdr);
        dest.writeValue(this.mProductRatingDesc);
        dest.writeValue(this.mProductRatingPoint);
        dest.writeValue(this.mProductReturnable);
        dest.writeValue(this.mProductReviewCount);
        dest.writeValue(this.mProductSoldCount);
        dest.writeValue(this.mProductStatus);
        dest.writeValue(this.mProductTalkCount);
        dest.writeString(this.mProductUrl);
        dest.writeValue(this.mProductWholesale);
        dest.writeValue(this.mShopFeaturedShop);
        dest.writeValue(this.mShopGoldStatus);
        dest.writeValue(this.mShopId);
        dest.writeValue(this.mShopIsOwner);
        dest.writeString(this.mShopLocation);
        dest.writeString(this.mShopLucky);
        dest.writeString(this.mShopName);
        dest.writeString(this.mShopUrl);
    }

    public RecentView() {
    }

    protected RecentView(Parcel in) {
        super(in);
        this.mBadges = new ArrayList<Badge>();
        in.readList(this.mBadges, Badge.class.getClassLoader());
        this.mLabels = new ArrayList<Label>();
        in.readList(this.mLabels, Label.class.getClassLoader());
        this.mProductAvailable = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductCurrency = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductCurrencyId = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductDepartmentId = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductEtalase = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductId = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductImage = in.readString();
        this.mProductImage300 = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductImageFull = in.readString();
        this.mProductName = in.readString();
        this.mProductNormalPrice = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductPreorder = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductPrice = in.readString();
        this.mProductPriceNoIdr = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductRatingDesc = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductRatingPoint = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductReturnable = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductReviewCount = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductSoldCount = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductStatus = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductTalkCount = (Long) in.readValue(Long.class.getClassLoader());
        this.mProductUrl = in.readString();
        this.mProductWholesale = (Long) in.readValue(Long.class.getClassLoader());
        this.mShopFeaturedShop = (Long) in.readValue(Long.class.getClassLoader());
        this.mShopGoldStatus = (Long) in.readValue(Long.class.getClassLoader());
        this.mShopId = (Long) in.readValue(Long.class.getClassLoader());
        this.mShopIsOwner = (Long) in.readValue(Long.class.getClassLoader());
        this.mShopLocation = in.readString();
        this.mShopLucky = in.readString();
        this.mShopName = in.readString();
        this.mShopUrl = in.readString();
    }

    public static final Creator<RecentView> CREATOR = new Creator<RecentView>() {
        @Override
        public RecentView createFromParcel(Parcel source) {
            return new RecentView(source);
        }

        @Override
        public RecentView[] newArray(int size) {
            return new RecentView[size];
        }
    };
}
