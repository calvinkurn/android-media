package com.tokopedia.flight.bookingV2.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 11/13/17.
 */

public class CartEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private Attribute attribute;
    @SerializedName("insurances")
    @Expose
    private List<InsuranceEntity> insurances;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public List<InsuranceEntity> getInsurances() {
        return insurances;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void setInsurances(List<InsuranceEntity> insurances) {
        this.insurances = insurances;
    }
}

