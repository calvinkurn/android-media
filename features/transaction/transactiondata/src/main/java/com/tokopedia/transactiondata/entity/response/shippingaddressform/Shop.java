package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transactiondata.entity.response.cartlist.shopgroup.GoldMerchant;
import com.tokopedia.transactiondata.entity.response.cartlist.shopgroup.OfficialStore;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class Shop {
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_status")
    @Expose
    private int shopStatus;
    @SerializedName("is_gold")
    @Expose
    private int isGold;
    @SerializedName("is_gold_badge")
    @Expose
    private boolean isGoldBadge;
    @SerializedName("is_official")
    @Expose
    private int isOfficial;
    @SerializedName("is_free_returns")
    @Expose
    private int isFreeReturns;
    @SerializedName("address_id")
    @Expose
    private int addressId;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("district_id")
    @Expose
    private int districtId;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("origin")
    @Expose
    private int origin;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("province_id")
    @Expose
    private int provinceId;
    @SerializedName("city_id")
    @Expose
    private int cityId;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("gold_merchant")
    @Expose
    private GoldMerchant goldMerchant;
    @SerializedName("official_store")
    @Expose
    private OfficialStore officialStore;

    public int getShopId() {
        return shopId;
    }

    public int getUserId() {
        return userId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public int getShopStatus() {
        return shopStatus;
    }

    public int getIsGold() {
        return isGold;
    }

    public boolean isGoldBadge() {
        return isGoldBadge;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public int getIsFreeReturns() {
        return isFreeReturns;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getDistrictId() {
        return districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public int getOrigin() {
        return origin;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public GoldMerchant getGoldMerchant() {
        return goldMerchant;
    }

    public OfficialStore getOfficialStore() {
        return officialStore;
    }
}
