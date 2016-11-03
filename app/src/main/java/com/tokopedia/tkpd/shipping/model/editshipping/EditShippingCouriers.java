package com.tokopedia.tkpd.shipping.model.editshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kris on 2/24/2016. Tokopedia
 */
public class EditShippingCouriers implements Parcelable{

    @SerializedName("shop_shipping")
    @Expose
    public ShopShipping shopShipping;
    @SerializedName("courier")
    @Expose
    public List<Courier> courier = new ArrayList<>();
    @SerializedName("provinces_cities_districts")
    @Expose
    public List<ProvinceCitiesDistrict> provincesCitiesDistricts = new ArrayList<>();

    protected EditShippingCouriers(Parcel in) {
        shopShipping = in.readParcelable(ShopShipping.class.getClassLoader());
        courier = in.createTypedArrayList(Courier.CREATOR);
        provincesCitiesDistricts = in.createTypedArrayList(ProvinceCitiesDistrict.CREATOR);
    }

    public EditShippingCouriers() {

    }

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

    public List<ProvinceCitiesDistrict> getProvincesCitiesDistricts() {
        return provincesCitiesDistricts;
    }

    public void setCourier(List<Courier> courier) {
        this.courier = courier;
    }

    public List<Courier> getCourier() {
        return courier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(shopShipping, i);
        parcel.writeTypedList(courier);
        parcel.writeTypedList(provincesCitiesDistricts);
    }
}
