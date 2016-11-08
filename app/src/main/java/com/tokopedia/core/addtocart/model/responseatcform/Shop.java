package com.tokopedia.core.addtocart.model.responseatcform;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Herdi_WORK on 21.09.16.
 */

public class Shop implements Parcelable {

    private static final String TAG = Shop.class.getSimpleName();

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ut")
    @Expose
    private Integer ut;
    @SerializedName("avail_shipping_code")
    @Expose
    private String availShippingCode;
    @SerializedName("origin_id")
    @Expose
    private Integer originId;
    @SerializedName("origin_postal")
    @Expose
    private String originPostal;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_gojek")
    @Expose
    private Integer isGojek;
    @SerializedName("is_ninja")
    @Expose
    private Integer isNinja;
    @SerializedName("show_oke")
    @Expose
    private Integer showOke;

    protected Shop(Parcel in){
        token = in.readString();
        ut    = in.readInt();
        availShippingCode   = in.readString();
        originId            = in.readInt();
        originPostal        = in.readString();
        latitude            = in.readString();
        longitude           = in.readString();
        device              = in.readString();
        from                = in.readString();
        name                = in.readString();
        isGojek             = in.readInt();
        isNinja             = in.readInt();
        showOke             = in.readInt();
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return The ut
     */
    public Integer getUt() {
        return ut;
    }

    /**
     * @param ut The ut
     */
    public void setUt(Integer ut) {
        this.ut = ut;
    }

    /**
     * @return The availShippingCode
     */
    public String getAvailShippingCode() {
        return availShippingCode;
    }

    /**
     * @param availShippingCode The avail_shipping_code
     */
    public void setAvailShippingCode(String availShippingCode) {
        this.availShippingCode = availShippingCode;
    }

    /**
     * @return The originId
     */
    public Integer getOriginId() {
        return originId;
    }

    /**
     * @param originId The origin_id
     */
    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    /**
     * @return The originPostal
     */
    public String getOriginPostal() {
        return originPostal;
    }

    /**
     * @param originPostal The origin_postal
     */
    public void setOriginPostal(String originPostal) {
        this.originPostal = originPostal;
    }

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The device
     */
    public String getDevice() {
        return device;
    }

    /**
     * @param device The device
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * @return The from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from The from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The isGojek
     */
    public Integer getIsGojek() {
        return isGojek;
    }

    /**
     * @param isGojek The is_gojek
     */
    public void setIsGojek(Integer isGojek) {
        this.isGojek = isGojek;
    }

    /**
     * @return The isNinja
     */
    public Integer getIsNinja() {
        return isNinja;
    }

    /**
     * @param isNinja The is_ninja
     */
    public void setIsNinja(Integer isNinja) {
        this.isNinja = isNinja;
    }

    /**
     * @return The showOke
     */
    public Integer getShowOke() {
        return showOke;
    }

    /**
     * @param showOke The show_oke
     */
    public void setShowOke(Integer showOke) {
        this.showOke = showOke;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(token);
        parcel.writeInt(ut);
        parcel.writeString(availShippingCode);
        parcel.writeInt(originId);
        parcel.writeString(originPostal);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(device);
        parcel.writeString(from);
        parcel.writeString(name);
        parcel.writeInt(isGojek);
        parcel.writeInt(isNinja);
        parcel.writeInt(showOke);
    }

    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>(){
        @Override
        public Shop createFromParcel(Parcel parcel) {
            return new Shop(parcel);
        }

        @Override
        public Shop[] newArray(int i) {
            return new Shop[i];
        }
    };

    @Override
    public String toString() {
        return TAG+" "+getName() +" "+getToken()+" "+getAvailShippingCode();
    }
}
