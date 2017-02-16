
package com.tokopedia.core.network.entity.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand implements Parcelable
{

    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("shop_defaultv3_url")
    @Expose
    private String shopDefaultv3Url;
    @SerializedName("shop_mobile_url")
    @Expose
    private String shopMobileUrl;
    @SerializedName("shop_apps_url")
    @Expose
    private String shopAppsUrl;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("logo_url")
    @Expose
    private String logoUrl;
    @SerializedName("microsite_url")
    @Expose
    private String micrositeUrl;
    public final static Parcelable.Creator<Brand> CREATOR = new Creator<Brand>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Brand createFromParcel(Parcel in) {
            Brand instance = new Brand();
            instance.shopId = ((int) in.readValue((int.class.getClassLoader())));
            instance.shopDefaultv3Url = ((String) in.readValue((String.class.getClassLoader())));
            instance.shopMobileUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.shopAppsUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.shopName = ((String) in.readValue((String.class.getClassLoader())));
            instance.logoUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.micrositeUrl = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Brand[] newArray(int size) {
            return (new Brand[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The shopId
     */
    public int getShopId() {
        return shopId;
    }

    /**
     * 
     * @param shopId
     *     The shop_id
     */
    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    /**
     * 
     * @return
     *     The shopDefaultv3Url
     */
    public String getShopDefaultv3Url() {
        return shopDefaultv3Url;
    }

    /**
     * 
     * @param shopDefaultv3Url
     *     The shop_defaultv3_url
     */
    public void setShopDefaultv3Url(String shopDefaultv3Url) {
        this.shopDefaultv3Url = shopDefaultv3Url;
    }

    /**
     * 
     * @return
     *     The shopMobileUrl
     */
    public String getShopMobileUrl() {
        return shopMobileUrl;
    }

    /**
     * 
     * @param shopMobileUrl
     *     The shop_mobile_url
     */
    public void setShopMobileUrl(String shopMobileUrl) {
        this.shopMobileUrl = shopMobileUrl;
    }

    /**
     * 
     * @return
     *     The shopAppsUrl
     */
    public String getShopAppsUrl() {
        return shopAppsUrl;
    }

    /**
     * 
     * @param shopAppsUrl
     *     The shop_apps_url
     */
    public void setShopAppsUrl(String shopAppsUrl) {
        this.shopAppsUrl = shopAppsUrl;
    }

    /**
     * 
     * @return
     *     The shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 
     * @param shopName
     *     The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 
     * @return
     *     The logoUrl
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * 
     * @param logoUrl
     *     The logo_url
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     * 
     * @return
     *     The micrositeUrl
     */
    public String getMicrositeUrl() {
        return micrositeUrl;
    }

    /**
     * 
     * @param micrositeUrl
     *     The microsite_url
     */
    public void setMicrositeUrl(String micrositeUrl) {
        this.micrositeUrl = micrositeUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(shopId);
        dest.writeValue(shopDefaultv3Url);
        dest.writeValue(shopMobileUrl);
        dest.writeValue(shopAppsUrl);
        dest.writeValue(shopName);
        dest.writeValue(logoUrl);
        dest.writeValue(micrositeUrl);
    }

    public int describeContents() {
        return  0;
    }

}
