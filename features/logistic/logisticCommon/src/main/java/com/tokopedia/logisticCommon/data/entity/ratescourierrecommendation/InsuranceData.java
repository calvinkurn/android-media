package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class InsuranceData implements Parcelable {

    @SerializedName("insurance_price")
    @Expose
    private int insurancePrice;
    @SerializedName("insurance_type")
    @Expose
    private int insuranceType;
    @SerializedName("insurance_type_info")
    @Expose
    private String insuranceTypeInfo;
    @SerializedName("insurance_used_type")
    @Expose
    private int insuranceUsedType;
    @SerializedName("insurance_used_info")
    @Expose
    private String insuranceUsedInfo;
    @SerializedName("insurance_used_default")
    @Expose
    private int insuranceUsedDefault;

    public InsuranceData() {
    }

    protected InsuranceData(Parcel in) {
        insurancePrice = in.readInt();
        insuranceType = in.readInt();
        insuranceTypeInfo = in.readString();
        insuranceUsedType = in.readInt();
        insuranceUsedInfo = in.readString();
        insuranceUsedDefault = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(insurancePrice);
        dest.writeInt(insuranceType);
        dest.writeString(insuranceTypeInfo);
        dest.writeInt(insuranceUsedType);
        dest.writeString(insuranceUsedInfo);
        dest.writeInt(insuranceUsedDefault);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InsuranceData> CREATOR = new Creator<InsuranceData>() {
        @Override
        public InsuranceData createFromParcel(Parcel in) {
            return new InsuranceData(in);
        }

        @Override
        public InsuranceData[] newArray(int size) {
            return new InsuranceData[size];
        }
    };

    public int getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(int insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public int getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(int insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceTypeInfo() {
        return insuranceTypeInfo;
    }

    public void setInsuranceTypeInfo(String insuranceTypeInfo) {
        this.insuranceTypeInfo = insuranceTypeInfo;
    }

    public int getInsuranceUsedType() {
        return insuranceUsedType;
    }

    public void setInsuranceUsedType(int insuranceUsedType) {
        this.insuranceUsedType = insuranceUsedType;
    }

    public String getInsuranceUsedInfo() {
        return insuranceUsedInfo;
    }

    public void setInsuranceUsedInfo(String insuranceUsedInfo) {
        this.insuranceUsedInfo = insuranceUsedInfo;
    }

    public int getInsuranceUsedDefault() {
        return insuranceUsedDefault;
    }

    public void setInsuranceUsedDefault(int insuranceUsedDefault) {
        this.insuranceUsedDefault = insuranceUsedDefault;
    }
}
