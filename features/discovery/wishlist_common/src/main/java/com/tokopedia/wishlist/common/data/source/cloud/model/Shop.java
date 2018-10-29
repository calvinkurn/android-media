package com.tokopedia.wishlist.common.data.source.cloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 20/09/18.
 */

public class Shop implements Parcelable {

    @SerializedName("id")
    String Id;
    @SerializedName("name")
    String Name;
    @SerializedName("url")
    String Url;
    @SerializedName("gold_merchant")
    boolean IsGoldMerchant;
    @SerializedName("official_store")
    boolean isOfficial;
    @SerializedName("location")
    String Location;
    @SerializedName("status")
    String Status;
    @SerializedName("lucky_merchant")
    String LuckyMerchant;

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

    public boolean isGoldMerchant() {
        return IsGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        IsGoldMerchant = goldMerchant;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLuckyMerchant() {
        return LuckyMerchant;
    }

    public void setLuckyMerchant(String luckyMerchant) {
        LuckyMerchant = luckyMerchant;
    }

    public Shop() {
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
        dest.writeValue(this.IsGoldMerchant);
        dest.writeValue(this.isOfficial);
        dest.writeString(this.Location);
        dest.writeString(this.Status);
        dest.writeString(this.LuckyMerchant);
    }

    protected Shop(Parcel in) {
        this.Id = in.readString();
        this.Name = in.readString();
        this.Url = in.readString();
        this.IsGoldMerchant = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isOfficial = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.Location = in.readString();
        this.Status = in.readString();
        this.LuckyMerchant = in.readString();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel source) {
            return new Shop(source);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

}
