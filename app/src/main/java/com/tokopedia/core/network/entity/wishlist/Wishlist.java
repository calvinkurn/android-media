package com.tokopedia.core.network.entity.wishlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class Wishlist {

    @SerializedName("id")
    String Id;
    @SerializedName("name")
    String Name;
    @SerializedName("url")
    String Url;
    @SerializedName("image")
    String ImageUrl;
    @SerializedName("price")
    int Price;
    @SerializedName("condition")
    String Condition;
    @SerializedName("available")
    Boolean isAvailable;
    @SerializedName("status")
    String Status;
    @SerializedName("price_formatted")
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
    public List<ProductItem.Badge> badges;
    @SerializedName("labels")
    @Expose
    public List<ProductItem.Label> labels;

    public List<ProductItem.Label> getLabels() {
        return labels;
    }

    public void setLabels(List<ProductItem.Label> labels) {
        this.labels = labels;
    }

    public List<ProductItem.Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<ProductItem.Badge> badges) {
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
}
