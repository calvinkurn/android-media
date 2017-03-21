
package com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProvincesCitiesDistrict {

    @SerializedName(value = "province_id", alternate = {"city_id", "district_id"})
    @Expose
    private Integer provinceId;
    @SerializedName(value = "cities", alternate = "districts")
    @Expose
    private List<ProvincesCitiesDistrict> cities = null;
    @SerializedName(value = "province_name", alternate = {"city_name", "district_name"})
    @Expose
    private String provinceName;

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public List<ProvincesCitiesDistrict> getCities() {
        return cities;
    }

    public void setCities(List<ProvincesCitiesDistrict> cities) {
        this.cities = cities;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
