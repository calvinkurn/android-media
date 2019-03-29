package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryPri on 15/05/17.
 */
@Deprecated
public class ShopShipment implements Parcelable {

    @SerializedName("shipping_id")
    @Expose
    private String shippingId;

    @SerializedName("shipping_name")
    @Expose
    private String shippingName;

    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("package_names")
    @Expose
    private List<String> packageNames;

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getPackageNames() {
        return packageNames;
    }

    public void setPackageNames(List<String> packageNames) {
        this.packageNames = packageNames;
    }

    protected ShopShipment(Parcel in) {
        shippingId = in.readString();
        shippingName = in.readString();
        logo = in.readString();
        if (in.readByte() == 0x01) {
            packageNames = new ArrayList<>();
            in.readStringList(packageNames);
        } else {
            packageNames = null;
        }
    }

    public static final Creator<ShopShipment> CREATOR = new Creator<ShopShipment>() {
        @Override
        public ShopShipment createFromParcel(Parcel in) {
            return new ShopShipment(in);
        }

        @Override
        public ShopShipment[] newArray(int size) {
            return new ShopShipment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shippingId);
        dest.writeString(shippingName);
        dest.writeString(logo);
        if (packageNames == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeStringList(packageNames);
        }
    }
}
