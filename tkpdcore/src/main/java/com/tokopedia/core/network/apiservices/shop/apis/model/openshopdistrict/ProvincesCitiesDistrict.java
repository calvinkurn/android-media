
package com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProvincesCitiesDistrict {

    @SerializedName("province_id")
    @Expose
    private Integer provinceId;
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;
    @SerializedName("province_name")
    @Expose
    private String provinceName;

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
