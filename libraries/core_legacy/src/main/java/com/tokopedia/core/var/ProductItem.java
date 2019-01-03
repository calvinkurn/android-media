package com.tokopedia.core.var;

import android.os.Parcelable;
import android.text.Spanned;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.topads.TopAds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.tkpd.library.utils.legacy.CurrencyFormatHelper.convertRupiahToInt;

/**
 * Created by Nathania on 4/06/15.
 * modified by m.normansyah - set type for certainly
 */
public class ProductItem extends RecyclerViewItem implements Serializable, Parcelable {

    public static final int PRODUCT_ITEM_TYPE = 192_012;
    public static final String CASHBACK = "cashback";
    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

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

    @SerializedName("rating")
    public String rating;

    @SerializedName("review_count")
    public String reviewCount;

    @SerializedName("official_store")
    public boolean isOfficial = false;

    public boolean productAlreadyWishlist;

    Spanned spannedName;

    Spanned spannedShop;

    Boolean isWishlist = false;

    Boolean isAvailable = true;

    Boolean isTopAds = false;

    TopAds topAds;
    private String trackerListName;
    private String trackerAttribution;

    private String originalPrice;
    private int discountPercentage;
    private int countCourier;
    private String cashback;

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

    public String getShopLocation() {
        return shop_location;
    }

    public void setShopLocation(String shop_location) {
        this.shop_location = shop_location;
    }

    public String getFree_return() {
        return free_return;
    }

    public void setFree_return(String free_return) {
        this.free_return = free_return;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isProductAlreadyWishlist() {
        return productAlreadyWishlist;
    }

    public void setProductAlreadyWishlist(boolean productAlreadyWishlist) {
        this.productAlreadyWishlist = productAlreadyWishlist;
    }

    public Boolean getOfficial() {
        return isOfficial;
    }

    public void setOfficial(Boolean official) {
        isOfficial = official;
    }

    public ProductItem() {
        setType(PRODUCT_ITEM_TYPE);
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getCountCourier() {
        return countCourier;
    }

    public void setCountCourier(int countCourier) {
        this.countCourier = countCourier;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeInt(this.isNewGold);
        dest.writeString(this.shop);
        dest.writeString(this.imgUri);
        dest.writeString(this.isGold);
        dest.writeString(this.luckyShop);
        dest.writeInt(this.shopId);
        dest.writeString(this.preorder);
        dest.writeString(this.wholesale);
        dest.writeList(this.labels);
        dest.writeTypedList(this.badges);
        dest.writeString(this.shop_location);
        dest.writeString(this.free_return);
        dest.writeString(this.rating);
        dest.writeString(this.reviewCount);
        dest.writeValue(this.isOfficial);
        dest.writeValue(this.productAlreadyWishlist);
        dest.writeParcelable((Parcelable) this.spannedName, flags);
        dest.writeParcelable((Parcelable) this.spannedShop, flags);
        dest.writeValue(this.isWishlist);
        dest.writeValue(this.isAvailable);
        dest.writeValue(this.isTopAds);
        dest.writeParcelable(this.topAds, flags);
        dest.writeString(this.trackerListName);
        dest.writeString(this.trackerAttribution);
        dest.writeInt(countCourier);
        dest.writeString(originalPrice);
        dest.writeInt(discountPercentage);
        dest.writeString(cashback);
    }

    protected ProductItem(android.os.Parcel in) {
        super(in);
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.isNewGold = in.readInt();
        this.shop = in.readString();
        this.imgUri = in.readString();
        this.isGold = in.readString();
        this.luckyShop = in.readString();
        this.shopId = in.readInt();
        this.preorder = in.readString();
        this.wholesale = in.readString();
        this.labels = new ArrayList<Label>();
        in.readList(this.labels, Label.class.getClassLoader());
        this.badges = in.createTypedArrayList(Badge.CREATOR);
        this.shop_location = in.readString();
        this.free_return = in.readString();
        this.rating = in.readString();
        this.reviewCount = in.readString();
        this.isOfficial = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.productAlreadyWishlist = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.spannedName = in.readParcelable(Spanned.class.getClassLoader());
        this.spannedShop = in.readParcelable(Spanned.class.getClassLoader());
        this.isWishlist = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isAvailable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isTopAds = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.topAds = in.readParcelable(TopAds.class.getClassLoader());
        this.trackerListName = in.readString();
        this.trackerAttribution = in.readString();
        this.countCourier = in.readInt();
        this.originalPrice = in.readString();
        this.discountPercentage = in.readInt();
        this.cashback = in.readString();
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(android.os.Parcel source) {
            return new ProductItem(source);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

    public void setTrackerListName(String trackerListName) {
        this.trackerListName = trackerListName;
    }

    public String getTrackerListName() {
        return trackerListName;
    }

    public void setTrackerAttribution(String trackerAttribution) {
        this.trackerAttribution = trackerAttribution;
    }

    public String getTrackerAttribution() {
        return trackerAttribution;
    }

    public Object getProductAsObjectDataLayerForWishlistClick(int position) {
        return DataLayer.mapOf(
                "name", getName(),
                "id", getId(),
                "price", Integer.toString(convertRupiahToInt(getPrice())),
                "brand", DEFAULT_VALUE_NONE_OTHER,
                "category", DEFAULT_VALUE_NONE_OTHER,
                "variant", DEFAULT_VALUE_NONE_OTHER,
                "position", Integer.toString(position)
        );
    }
}