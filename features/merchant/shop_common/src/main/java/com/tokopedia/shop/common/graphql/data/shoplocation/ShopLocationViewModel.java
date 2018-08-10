package com.tokopedia.shop.common.graphql.data.shoplocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopLocationViewModel {
    private String id;
    private String name;
    private String address;
    private Integer districtId;
    private String districtName;
    private Integer cityId;
    private String cityName;
    private Integer stateId;
    private String stateName;
    private Integer postalCode;
    private String email;
    private String phone;
    private String fax;

    public ShopLocationViewModel(ShopLocationModel shopLocationModel) {
        this.id = shopLocationModel.getId();
        this.name = shopLocationModel.getName();
        this.address = shopLocationModel.getAddress();
        this.districtId = shopLocationModel.getDistrictId();
        this.districtName = shopLocationModel.getDistrictName();
        this.cityId = shopLocationModel.getCityId();
        this.cityName = shopLocationModel.getCityName();
        this.stateId = shopLocationModel.getStateId();
        this.stateName = shopLocationModel.getStateName();
        this.postalCode = shopLocationModel.getPostalCode();
        this.email = shopLocationModel.getEmail();
        this.phone = shopLocationModel.getPhone();
        this.fax = shopLocationModel.getFax();
    }

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
