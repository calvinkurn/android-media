package com.tokopedia.sellerapp.home.model.wishlist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ricoharisin on 4/15/16.
 */
public class Shop {
    @SerializedName("id")
    String Id;
    @SerializedName("name")
    String Name;
    @SerializedName("url")
    String Url;
    @SerializedName("gold_merchant")
    Boolean IsGoldMerchant;
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
}
