package com.tokopedia.tkpd.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 10/18/16.
 */

public class CatalogDetailItemShop implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("is_gold_shop")
    @Expose
    private String isGoldShop;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("image_uri")
    @Expose
    private String imageUri;
    @SerializedName("reputation_image_uri")
    @Expose
    private String reputationImageUri;
    @SerializedName("shop_lucky")
    @Expose
    private String shopLucky;
    @SerializedName("is_official")
    @Expose
    private String isOfficial;

    protected CatalogDetailItemShop(Parcel in) {
        id = in.readString();
        name = in.readString();
        domain = in.readString();
        city = in.readString();
        isGoldShop = in.readString();
        uri = in.readString();
        imageUri = in.readString();
        reputationImageUri = in.readString();
        shopLucky = in.readString();
        isOfficial = in.readString();
    }

    public CatalogDetailItemShop() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(domain);
        dest.writeString(city);
        dest.writeString(isGoldShop);
        dest.writeString(uri);
        dest.writeString(imageUri);
        dest.writeString(reputationImageUri);
        dest.writeString(shopLucky);
        dest.writeString(isOfficial);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CatalogDetailItemShop> CREATOR = new Parcelable.Creator<CatalogDetailItemShop>() {
        @Override
        public CatalogDetailItemShop createFromParcel(Parcel in) {
            return new CatalogDetailItemShop(in);
        }

        @Override
        public CatalogDetailItemShop[] newArray(int size) {
            return new CatalogDetailItemShop[size];
        }
    };

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsGoldShop() {
        return isGoldShop;
    }

    public void setIsGoldShop(String isGoldShop) {
        this.isGoldShop = isGoldShop;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getReputationImageUri() {
        return reputationImageUri;
    }

    public void setReputationImageUri(String reputationImageUri) {
        this.reputationImageUri = reputationImageUri;
    }

    public String getShopLucky() {
        return shopLucky;
    }

    public void setShopLucky(String shopLucky) {
        this.shopLucky = shopLucky;
    }

    public String getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(String isOfficial) {
        this.isOfficial = isOfficial;
    }
}