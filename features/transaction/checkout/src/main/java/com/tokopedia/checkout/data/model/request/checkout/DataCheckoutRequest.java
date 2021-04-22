package com.tokopedia.checkout.data.model.request.checkout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class DataCheckoutRequest implements Parcelable {
    @SerializedName("address_id")
    @Expose
    public String addressId;
    @SerializedName("shop_products")
    @Expose
    public List<ShopProductCheckoutRequest> shopProducts = new ArrayList<>();

    public DataCheckoutRequest() {
    }

    private DataCheckoutRequest(Builder builder) {
        addressId = builder.addressId;
        shopProducts = builder.shopProducts;
    }

    protected DataCheckoutRequest(Parcel in) {
        addressId = in.readString();
        shopProducts = in.createTypedArrayList(ShopProductCheckoutRequest.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressId);
        dest.writeTypedList(shopProducts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataCheckoutRequest> CREATOR = new Creator<DataCheckoutRequest>() {
        @Override
        public DataCheckoutRequest createFromParcel(Parcel in) {
            return new DataCheckoutRequest(in);
        }

        @Override
        public DataCheckoutRequest[] newArray(int size) {
            return new DataCheckoutRequest[size];
        }
    };

    public static final class Builder {
        private String addressId;
        private List<ShopProductCheckoutRequest> shopProducts;

        public Builder() {
        }

        public Builder addressId(String val) {
            addressId = val;
            return this;
        }

        public Builder shopProducts(List<ShopProductCheckoutRequest> val) {
            shopProducts = val;
            return this;
        }

        public DataCheckoutRequest build() {
            return new DataCheckoutRequest(this);
        }
    }
}
