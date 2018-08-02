package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class InsuranceData {

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
