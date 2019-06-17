package com.tokopedia.checkout.domain.datamodel.recentview;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

public class RecentView implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("product_rating")
    @Expose
    private int productRating;
    @SerializedName("product_review_count")
    @Expose
    private int productReviewCount;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("wishlist")
    @Expose
    private boolean wishlist;

    public RecentView() {
    }

    protected RecentView(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        shopUrl = in.readString();
        productRating = in.readInt();
        productReviewCount = in.readInt();
        productImage = in.readString();
        shopId = in.readString();
        productPrice = in.readString();
        wishlist = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(shopUrl);
        dest.writeInt(productRating);
        dest.writeInt(productReviewCount);
        dest.writeString(productImage);
        dest.writeString(shopId);
        dest.writeString(productPrice);
        dest.writeByte((byte) (wishlist ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecentView> CREATOR = new Creator<RecentView>() {
        @Override
        public RecentView createFromParcel(Parcel in) {
            return new RecentView(in);
        }

        @Override
        public RecentView[] newArray(int size) {
            return new RecentView[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public int getProductRating() {
        return productRating;
    }

    public void setProductRating(int productRating) {
        this.productRating = productRating;
    }

    public int getProductReviewCount() {
        return productReviewCount;
    }

    public void setProductReviewCount(int productReviewCount) {
        this.productReviewCount = productReviewCount;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isWishlist() {
        return wishlist;
    }
}
