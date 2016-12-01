package com.tokopedia.core.var;

import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.topads.TopAds;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathania on 4/06/15.
 * modified by m.normansyah - set type for certainly
 */
@Parcel
public class ProductItem extends RecyclerViewItem implements Serializable{

    public static final int PRODUCT_ITEM_TYPE = 192_012;

    @SerializedName("product_id")
    @Expose
    public String id;// 1

    @SerializedName("product_name")
    @Expose
    public String name;// 2

    @SerializedName("product_price")
    @Expose
    public String price;// 3

    @SerializedName("shop_gold_status")
    @Expose
    public int isNewGold;// 4

    @SerializedName("shop_name")
    @Expose
    public String shop;// 5

    @SerializedName("product_image")
    @Expose
    public String imgUri;// 6

    public String isGold;// this is replace by isNewGold

    @SerializedName("shop_lucky")
    @Expose
    public String luckyShop;// 7

    @SerializedName("shop_id")
    @Expose
    int shopId;// 8


    @SerializedName("product_preorder")
    @Expose
    public String preorder;


    @SerializedName("product_wholesale")
    public String wholesale;

    @SerializedName("labels")
    @Expose
    public List<Label> labels = new ArrayList<Label>();

    @SerializedName("badges")
    @Expose
    public List<Badge> badges = new ArrayList<Badge>();

    @SerializedName("shop_location")
    public String shop_location;

    @SerializedName("free_return")
    public String free_return;

    @Transient
    Spanned spannedName;

    @Transient
    Spanned spannedShop;

    Boolean isWishlist = false;

    Boolean isAvailable = true;

    Boolean isTopAds = false;

    @Transient
    TopAds topAds;

    /**
     *
     * @return
     * The badges
     */
    public List<Badge> getBadges() {
        return badges;
    }

    /**
     *
     * @param badges
     * The badges
     */
    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsNewGold() {
        return isNewGold;
    }

    public void setIsNewGold(int isNewGold) {
        this.isNewGold = isNewGold;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getIsGold() {
        return isGold;
    }

    public void setIsGold(String isGold) {
        this.isGold = isGold;
    }

    public String getLuckyShop() {
        return luckyShop;
    }

    public void setLuckyShop(String luckyShop) {
        this.luckyShop = luckyShop;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public Spanned getSpannedName() {
        return spannedName;
    }

    public void setSpannedName(Spanned spannedName) {
        this.spannedName = spannedName;
    }

    public Spanned getSpannedShop() {
        return spannedShop;
    }

    public void setSpannedShop(Spanned spannedShop) {
        this.spannedShop = spannedShop;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getWholesale() {
        return wholesale;
    }

    public void setWholesale(String wholesale) {
        this.wholesale = wholesale;
    }

    public String getPreorder() {
        return preorder;
    }

    public void setPreorder(String preorder) {
        this.preorder = preorder;
    }

    public Boolean getIsTopAds() {
        return isTopAds;
    }

    public void setIsTopAds(Boolean isTopAds) {
        this.isTopAds = isTopAds;
    }

    public TopAds getTopAds() {
        return topAds;
    }

    public void setTopAds(TopAds topAds) {
        this.topAds = topAds;
    }

    public String getShop_location() {
        return shop_location;
    }

    public void setShop_location(String shop_location) {
        this.shop_location = shop_location;
    }

    public String getFree_return() {
        return free_return;
    }

    public void setFree_return(String free_return) {
        this.free_return = free_return;
    }

    public ProductItem() {
        setType(PRODUCT_ITEM_TYPE);
    }

    public ProductItem(String name, String price, String shop, String isGold, String imgUri) {
        this();
        this.name = name;
        this.price = price;
        this.shop = shop;
        this.isGold = isGold;
        this.imgUri = imgUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductItem that = (ProductItem) o;

        if (isNewGold != that.isNewGold) return false;
        if (shopId != that.shopId) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (shop != null ? !shop.equals(that.shop) : that.shop != null) return false;
        if (imgUri != null ? !imgUri.equals(that.imgUri) : that.imgUri != null) return false;
        if (isGold != null ? !isGold.equals(that.isGold) : that.isGold != null) return false;
        return !(luckyShop != null ? !luckyShop.equals(that.luckyShop) : that.luckyShop != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + isNewGold;
        result = 31 * result + (shop != null ? shop.hashCode() : 0);
        result = 31 * result + (imgUri != null ? imgUri.hashCode() : 0);
        result = 31 * result + (isGold != null ? isGold.hashCode() : 0);
        result = 31 * result + (luckyShop != null ? luckyShop.hashCode() : 0);
        result = 31 * result + shopId;
        return result;
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", shop='" + shop + '\'' +
                ", imgUri='" + imgUri + '\'' +
                ", isGold='" + isGold + '\'' +
                ", luckyShop='" + luckyShop + '\'' +
                '}';
    }

    public Boolean getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(Boolean isWishlist) {
        this.isWishlist = isWishlist;
    }

    @Parcel
    public static class Label {
        @SerializedName("title")
        @Expose
        String title;
        @SerializedName("color")
        @Expose
        String color;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    @Parcel
    public static class Badge extends RecyclerViewItem {

        @SerializedName("title")
        @Expose
        String title;
        @SerializedName("img_url")
        @Expose
        String imgUrl;

        //sometimes it's different at ws (sometimes image_url and sometimes imgurl)
        @SerializedName("image_url")
        @Expose
        String imageUrl;


        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        /**
         *
         * @return
         * The title
         */
        public String getTitle() {
            return title;
        }

        /**
         *
         * @param title
         * The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         *
         * @return
         * The imgUrl
         */
        public String getImgUrl() {
            return imgUrl;
        }

        /**
         *
         * @param imgUrl
         * The img_url
         */
        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

    }
}