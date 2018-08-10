package com.tokopedia.shop.common.graphql.data.shoplocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopLocationModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("districtId")
    @Expose
    private Integer districtId;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("cityId")
    @Expose
    private Integer cityId;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("stateId")
    @Expose
    private Integer stateId;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("postalCode")
    @Expose
    private Integer postalCode;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fax")
    @Expose
    private String fax;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }
}
