package com.tokopedia.wishlist.common.data.source.cloud.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class Wishlist implements Parcelable {

    @SerializedName("id")
    String Id;
    @SerializedName("name")
    String Name;
    @SerializedName("url")
    String Url;
    @SerializedName("image_url")
    String ImageUrl;
    @SerializedName("raw_price")
    @SuppressLint("Invalid Data Type")
    String Price;
    @SerializedName("condition")
    String Condition;
    @SerializedName("available")
    Boolean isAvailable;
    @SerializedName("status")
    String Status;
    @SerializedName("price")
    @SuppressLint("Invalid Data Type")
    String PriceFmt;
    @SerializedName("minimum_order")
    int MinimumOrder;
    @SerializedName("shop")
    Shop Shop;
    @SerializedName("preorder")
    Boolean isPreOrder;
    @SerializedName("badges")
    @Expose
    public List<Badge> badges;
    @SerializedName("labels")
    @Expose
    public List<Label> labels;
    @SerializedName("rating")
    @Expose
    public int rating;
    @SerializedName("review_count")
    @Expose
    public int reviewCount;
    @SerializedName("category_breadcrumb")
    @Expose
    public String categoryBreadcrumb = "";
    @SerializedName("free_ongkir")
    @Expose
    public FreeOngkir freeOngkir;
    @SerializedName("free_ongkir_extra")
    @Expose
    public FreeOngkir freeOngkirExtra;

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Shop getShop() {
        return Shop;
    }

    public void setShop(Shop shop) {
        Shop = shop;
    }

    public String getPriceFmt() {
        return PriceFmt;
    }

    public void setPriceFmt(String priceFmt) {
        PriceFmt = priceFmt;
    }

    public int getMinimumOrder() {
        return MinimumOrder;
    }

    public void setMinimumOrder(int minimumOrder) {
        MinimumOrder = minimumOrder;
    }

    public Boolean getIsPreOrder() {
        return isPreOrder;
    }

    public void setIsPreOrder(Boolean isPreOrder) {
        this.isPreOrder = isPreOrder;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }

    public void setCategoryBreadcrumb(String categoryBreadcrumb) {
        this.categoryBreadcrumb = categoryBreadcrumb;
    }

    public FreeOngkir getFreeOngkir() {
        return freeOngkir;
    }

    public Wishlist() {
    }

    protected Wishlist(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Url = in.readString();
        ImageUrl = in.readString();
        Price = in.readString();
        Condition = in.readString();
        byte tmpIsAvailable = in.readByte();
        isAvailable = tmpIsAvailable == 0 ? null : tmpIsAvailable == 1;
        Status = in.readString();
        PriceFmt = in.readString();
        MinimumOrder = in.readInt();
        Shop = in.readParcelable(com.tokopedia.wishlist.common.data.source.cloud.model.Shop.class.getClassLoader());
        byte tmpIsPreOrder = in.readByte();
        isPreOrder = tmpIsPreOrder == 0 ? null : tmpIsPreOrder == 1;
        badges = in.createTypedArrayList(Badge.CREATOR);
        labels = in.createTypedArrayList(Label.CREATOR);
        rating = in.readInt();
        reviewCount = in.readInt();
        categoryBreadcrumb = in.readString();
        freeOngkir = in.readParcelable(FreeOngkir.class.getClassLoader());
        freeOngkirExtra = in.readParcelable(FreeOngkir.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(Url);
        dest.writeString(ImageUrl);
        dest.writeString(Price);
        dest.writeString(Condition);
        dest.writeByte((byte) (isAvailable == null ? 0 : isAvailable ? 1 : 2));
        dest.writeString(Status);
        dest.writeString(PriceFmt);
        dest.writeInt(MinimumOrder);
        dest.writeParcelable(Shop, flags);
        dest.writeByte((byte) (isPreOrder == null ? 0 : isPreOrder ? 1 : 2));
        dest.writeTypedList(badges);
        dest.writeTypedList(labels);
        dest.writeInt(rating);
        dest.writeInt(reviewCount);
        dest.writeString(categoryBreadcrumb);
        dest.writeParcelable(freeOngkir, flags);
        dest.writeParcelable(freeOngkirExtra, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Wishlist> CREATOR = new Creator<Wishlist>() {
        @Override
        public Wishlist createFromParcel(Parcel in) {
            return new Wishlist(in);
        }

        @Override
        public Wishlist[] newArray(int size) {
            return new Wishlist[size];
        }
    };
}
