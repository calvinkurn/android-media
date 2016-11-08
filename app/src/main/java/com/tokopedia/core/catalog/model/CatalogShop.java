package com.tokopedia.core.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogShop implements Parcelable {

    @SerializedName("shop_reputation")
    @Expose
    private ShopReputation shopReputation;
    @SerializedName("is_gold_shop")
    @Expose
    private int isGoldShop;
    @SerializedName("shop_lucky")
    @Expose
    private String shopLucky;
    @SerializedName("shop_rate_accuracy")
    @Expose
    private String shopRateAccuracy;
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain;
    @SerializedName("shop_rating")
    @Expose
    private String shopRating;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_rate_speed")
    @Expose
    private String shopRateSpeed;
    @SerializedName("shop_total_product")
    @Expose
    private String shopTotalProduct;
    @SerializedName("shop_is_owner")
    @Expose
    private int shopIsOwner;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_location")
    @Expose
    private String shopLocation;
    @SerializedName("shop_rate_service")
    @Expose
    private String shopRateService;
    @SerializedName("shop_rating_desc")
    @Expose
    private String shopRatingDesc;
    @SerializedName("shop_uri")
    @Expose
    private String shopUri;
    @SerializedName("product_list")
    @Expose
    private List<ProductCatalog> productList = new ArrayList<>();
    @SerializedName("shop_total_address")
    @Expose
    private String shopTotalAddress;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;

    public ShopReputation getShopReputation() {
        return shopReputation;
    }

    public int getIsGoldShop() {
        return isGoldShop;
    }

    public String getShopLucky() {
        return shopLucky;
    }

    public String getShopRateAccuracy() {
        return shopRateAccuracy;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public String getShopRating() {
        return shopRating;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopRateSpeed() {
        return shopRateSpeed;
    }

    public String getShopTotalProduct() {
        return shopTotalProduct;
    }

    public int getShopIsOwner() {
        return shopIsOwner;
    }

    public String getShopId() {
        return shopId;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public String getShopRateService() {
        return shopRateService;
    }

    public String getShopRatingDesc() {
        return shopRatingDesc;
    }

    public String getShopUri() {
        return shopUri;
    }

    public List<ProductCatalog> getProductList() {
        return productList;
    }

    public String getShopTotalAddress() {
        return shopTotalAddress;
    }

    public String getShopImage() {
        return shopImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.shopReputation, flags);
        dest.writeInt(this.isGoldShop);
        dest.writeString(this.shopLucky);
        dest.writeString(this.shopRateAccuracy);
        dest.writeString(this.shopDomain);
        dest.writeString(this.shopRating);
        dest.writeString(this.shopName);
        dest.writeString(this.shopRateSpeed);
        dest.writeString(this.shopTotalProduct);
        dest.writeInt(this.shopIsOwner);
        dest.writeString(this.shopId);
        dest.writeString(this.shopLocation);
        dest.writeString(this.shopRateService);
        dest.writeString(this.shopRatingDesc);
        dest.writeString(this.shopUri);
        dest.writeList(this.productList);
        dest.writeString(this.shopTotalAddress);
        dest.writeString(this.shopImage);
    }

    public CatalogShop() {
    }

    protected CatalogShop(Parcel in) {
        this.shopReputation = in.readParcelable(ShopReputation.class.getClassLoader());
        this.isGoldShop = in.readInt();
        this.shopLucky = in.readString();
        this.shopRateAccuracy = in.readString();
        this.shopDomain = in.readString();
        this.shopRating = in.readString();
        this.shopName = in.readString();
        this.shopRateSpeed = in.readString();
        this.shopTotalProduct = in.readString();
        this.shopIsOwner = in.readInt();
        this.shopId = in.readString();
        this.shopLocation = in.readString();
        this.shopRateService = in.readString();
        this.shopRatingDesc = in.readString();
        this.shopUri = in.readString();
        this.productList = new ArrayList<ProductCatalog>();
        in.readList(this.productList, ProductCatalog.class.getClassLoader());
        this.shopTotalAddress = in.readString();
        this.shopImage = in.readString();
    }

    public static final Parcelable.Creator<CatalogShop> CREATOR = new Parcelable.Creator<CatalogShop>() {
        @Override
        public CatalogShop createFromParcel(Parcel source) {
            return new CatalogShop(source);
        }

        @Override
        public CatalogShop[] newArray(int size) {
            return new CatalogShop[size];
        }
    };
}
