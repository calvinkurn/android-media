package com.tokopedia.core.network.entity.discovery;

import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * @author kulomady on 11/26/16.
 */

@Deprecated
public class ShopModel extends RecyclerViewItem {
    public static final int SHOP_MODEL_TYPE = 1_912_123;
    String shopImage;
    String shopName;
    String totalTransaction;
    String numberOfFavorite;
    String shopId;
    String isGold;
    String luckyImage;
    String location;
    String reputationImageUrl;
    String shopDomain;
    List<String> productImages;
    boolean isOfficial;
    boolean isFavorited;

    public ShopModel() {
        setType(SHOP_MODEL_TYPE);
    }

    public ShopModel(BrowseShopModel.Shops shop) {
        this();
        shopImage = shop.shopImage;
        shopName = shop.shopName;
        totalTransaction = shop.shopTotalTransaction;
        numberOfFavorite = shop.shopTotalFavorite;
        shopId = shop.shopId;
        isGold = shop.shopGoldShop;
        luckyImage = shop.shopLucky;
        isOfficial = shop.isOfficial;
        isFavorited = shop.isFavorited;
        location = shop.shopLocation;
        reputationImageUrl = shop.reputationImageUri;
        shopDomain = shop.shopDomain;
        productImages = shop.productImages;
    }

    public String getShopImage() {
        return shopImage;
    }

    public String getShopName() {
        return shopName;
    }

    public String getTotalTransaction() {
        return totalTransaction;
    }

    public String getNumberOfFavorite() {
        return numberOfFavorite;
    }

    public String getShopId() {
        return shopId;
    }

    public String getIsGold() {
        return isGold;
    }

    public String getLuckyImage() {
        return luckyImage;
    }

    public String getLocation() {
        return location;
    }

    public String getReputationImageUrl() {
        return reputationImageUrl;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public List<String> getProductImages() {
        return productImages;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.shopImage);
        dest.writeString(this.shopName);
        dest.writeString(this.totalTransaction);
        dest.writeString(this.numberOfFavorite);
        dest.writeString(this.shopId);
        dest.writeString(this.isGold);
        dest.writeString(this.luckyImage);
        dest.writeString(this.location);
        dest.writeString(this.reputationImageUrl);
        dest.writeString(this.shopDomain);
        dest.writeStringList(this.productImages);
        dest.writeByte(this.isOfficial ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFavorited ? (byte) 1 : (byte) 0);
    }

    protected ShopModel(android.os.Parcel in) {
        super(in);
        this.shopImage = in.readString();
        this.shopName = in.readString();
        this.totalTransaction = in.readString();
        this.numberOfFavorite = in.readString();
        this.shopId = in.readString();
        this.isGold = in.readString();
        this.luckyImage = in.readString();
        this.location = in.readString();
        this.reputationImageUrl = in.readString();
        this.shopDomain = in.readString();
        in.readStringList(this.productImages);
        this.isOfficial = in.readByte() != 0;
        this.isFavorited = in.readByte() != 0;
    }

    public static final Creator<ShopModel> CREATOR = new Creator<ShopModel>() {
        @Override
        public ShopModel createFromParcel(android.os.Parcel source) {
            return new ShopModel(source);
        }

        @Override
        public ShopModel[] newArray(int size) {
            return new ShopModel[size];
        }
    };
}
