package com.tokopedia.core.network.entity.wishlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricoharisin on 4/15/16.
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
    int Price;
    @SerializedName("condition")
    String Condition;
    @SerializedName("available")
    Boolean isAvailable;
    @SerializedName("status")
    String Status;
    @SerializedName("price")
    String PriceFmt;
    @SerializedName("minimum_order")
    int MinimumOrder;

    @SerializedName("wholesale_price")
    List<WholesalePrice> Wholesale = new ArrayList<>();
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

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
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

    public List<WholesalePrice> getWholesale() {
        return Wholesale;
    }

    public void setWholesale(List<WholesalePrice> wholesale) {
        Wholesale = wholesale;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id);
        dest.writeString(this.Name);
        dest.writeString(this.Url);
        dest.writeString(this.ImageUrl);
        dest.writeInt(this.Price);
        dest.writeString(this.Condition);
        dest.writeValue(this.isAvailable);
        dest.writeString(this.Status);
        dest.writeString(this.PriceFmt);
        dest.writeInt(this.MinimumOrder);
        dest.writeTypedList(this.Wholesale);
        dest.writeParcelable(this.Shop, flags);
        dest.writeValue(this.isPreOrder);
        dest.writeTypedList(this.badges);
        dest.writeTypedList(this.labels);
    }

    public Wishlist() {
    }

    protected Wishlist(Parcel in) {
        this.Id = in.readString();
        this.Name = in.readString();
        this.Url = in.readString();
        this.ImageUrl = in.readString();
        this.Price = in.readInt();
        this.Condition = in.readString();
        this.isAvailable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.Status = in.readString();
        this.PriceFmt = in.readString();
        this.MinimumOrder = in.readInt();
        this.Wholesale = in.createTypedArrayList(WholesalePrice.CREATOR);
        this.Shop = in.readParcelable(com.tokopedia.core.network.entity.wishlist.Shop.class.getClassLoader());
        this.isPreOrder = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.badges = in.createTypedArrayList(Badge.CREATOR);
        this.labels = in.createTypedArrayList(Label.CREATOR);
    }

    public static final Creator<Wishlist> CREATOR = new Creator<Wishlist>() {
        @Override
        public Wishlist createFromParcel(Parcel source) {
            return new Wishlist(source);
        }

        @Override
        public Wishlist[] newArray(int size) {
            return new Wishlist[size];
        }
    };
}
