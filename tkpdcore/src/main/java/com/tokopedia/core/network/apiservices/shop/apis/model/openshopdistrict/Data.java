
package com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("provinces_cities_districts")
    @Expose
    private List<ProvincesCitiesDistrict> provincesCitiesDistricts = null;

    public List<ProvincesCitiesDistrict> getProvincesCitiesDistricts() {
        return provincesCitiesDistricts;
    }

    public void setProvincesCitiesDistricts(List<ProvincesCitiesDistrict> provincesCitiesDistricts) {
        this.provincesCitiesDistricts = provincesCitiesDistricts;
    }

}
