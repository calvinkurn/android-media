package com.tokopedia.editshipping.domain.model.editshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.logisticCommon.data.entity.address.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kris on 2/24/2016. Tokopedia
 */
public class EditShippingCouriers implements Parcelable {

    public static final Creator<EditShippingCouriers> CREATOR = new Creator<EditShippingCouriers>() {
        @Override
        public EditShippingCouriers createFromParcel(Parcel in) {
            return new EditShippingCouriers(in);
        }

        @Override
        public EditShippingCouriers[] newArray(int size) {
            return new EditShippingCouriers[size];
        }
    };
    @SerializedName("shop_shipping")
    @Expose
    public ShopShipping shopShipping;
    @SerializedName("courier")
    @Expose
    public List<Courier> courier = new ArrayList<>();
    @SerializedName("provinces_cities_districts")
    @Expose
    public List<ProvinceCitiesDistrict> provincesCitiesDistricts = new ArrayList<>();
    @SerializedName("token")
    @Expose
    private Token token;

    protected EditShippingCouriers(Parcel in) {
        token = in.readParcelable(Token.class.getClassLoader());
        shopShipping = in.readParcelable(ShopShipping.class.getClassLoader());
        courier = in.createTypedArrayList(Courier.CREATOR);
        provincesCitiesDistricts = in.createTypedArrayList(ProvinceCitiesDistrict.CREATOR);
    }

    public EditShippingCouriers() {

    }

    public List<ProvinceCitiesDistrict> getProvincesCitiesDistricts() {
        return provincesCitiesDistricts;
    }

    public List<Courier> getCourier() {
        return courier;
    }

    public void setCourier(List<Courier> courier) {
        this.courier = courier;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(token, i);
        parcel.writeParcelable(shopShipping, i);
        parcel.writeTypedList(courier);
        parcel.writeTypedList(provincesCitiesDistricts);
    }
}
