
package com.tokopedia.tkpd.shipping.model.openshopshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CannotCreate implements Parcelable {

    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("shop_name")
    @Expose
    private String shopName;



    /**
     * 
     * @return
     *     The domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 
     * @param domain
     *     The domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.domain);
        dest.writeString(this.shopName);
    }

    public CannotCreate() {
    }

    protected CannotCreate(Parcel in) {
        this.domain = in.readString();
        this.shopName = in.readString();
    }

    public static final Parcelable.Creator<CannotCreate> CREATOR = new Parcelable.Creator<CannotCreate>() {
        @Override
        public CannotCreate createFromParcel(Parcel source) {
            return new CannotCreate(source);
        }

        @Override
        public CannotCreate[] newArray(int size) {
            return new CannotCreate[size];
        }
    };
}
