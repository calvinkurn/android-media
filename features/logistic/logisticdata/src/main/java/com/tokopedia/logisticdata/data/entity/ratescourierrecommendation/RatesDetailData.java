package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class RatesDetailData implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rates_id")
    @Expose
    private String ratesId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("services")
    @Expose
    private List<ServiceData> services;
    @SerializedName("error")
    @Expose
    private ErrorRatesDetailData error;
    @SerializedName("promo_stacking")
    @Expose
    private PromoStacking promoStacking;

    public RatesDetailData() {
    }

    protected RatesDetailData(Parcel in) {
        id = in.readString();
        ratesId = in.readString();
        type = in.readString();
        services = in.createTypedArrayList(ServiceData.CREATOR);
        error = in.readParcelable(ErrorRatesDetailData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ratesId);
        dest.writeString(type);
        dest.writeTypedList(services);
        dest.writeParcelable(error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RatesDetailData> CREATOR = new Creator<RatesDetailData>() {
        @Override
        public RatesDetailData createFromParcel(Parcel in) {
            return new RatesDetailData(in);
        }

        @Override
        public RatesDetailData[] newArray(int size) {
            return new RatesDetailData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRatesId() {
        return ratesId;
    }

    public void setRatesId(String ratesId) {
        this.ratesId = ratesId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ServiceData> getServices() {
        return services;
    }

    public void setServices(List<ServiceData> services) {
        this.services = services;
    }

    public ErrorRatesDetailData getError() {
        return error;
    }

    public void setError(ErrorRatesDetailData error) {
        this.error = error;
    }

    public PromoStacking getPromoStacking() {
        return promoStacking;
    }

    public void setPromoStacking(PromoStacking promoStacking) {
        this.promoStacking = promoStacking;
    }
}
