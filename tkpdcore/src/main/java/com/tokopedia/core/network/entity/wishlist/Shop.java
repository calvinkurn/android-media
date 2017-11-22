package com.tokopedia.core.network.entity.wishlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class Shop implements Parcelable {
    @SerializedName("id")
    String Id;
    @SerializedName("name")
    String Name;
    @SerializedName("url")
    String Url;
    @SerializedName("gold_merchant")
    Boolean IsGoldMerchant;
    @SerializedName("official_store")
    Boolean isOfficial;
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

    public Boolean getIsGoldMerchant() {
        return IsGoldMerchant;
    }

    public void setIsGoldMerchant(Boolean isGoldMerchant) {
        IsGoldMerchant = isGoldMerchant;
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

    public Boolean getOfficial() {
        return isOfficial;
    }

    public void setOfficial(Boolean official) {
        isOfficial = official;
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

    public Shop() {
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

    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
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
