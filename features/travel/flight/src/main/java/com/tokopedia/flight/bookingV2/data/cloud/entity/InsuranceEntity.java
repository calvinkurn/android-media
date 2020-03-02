package com.tokopedia.flight.bookingV2.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsuranceEntity {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("total_price_numeric")
    @Expose
    private long totalPriceNumeric;
    @SerializedName("default_checked")
    @Expose
    private boolean defaultChecked;
    @SerializedName("tnc_agreement")
    @Expose
    private String tncAggreement;
    @SerializedName("tnc_url")
    @Expose
    private String tncUrl;
    @SerializedName("benefits")
    @Expose
    private List<BenefitEntity> benefits;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDefaultChecked() {
        return defaultChecked;
    }

    public String getTncAggreement() {
        return tncAggreement;
    }

    public String getTncUrl() {
        return tncUrl;
    }

    public List<BenefitEntity> getBenefits() {
        return benefits;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTotalPriceNumeric() {
        return totalPriceNumeric;
    }
}
