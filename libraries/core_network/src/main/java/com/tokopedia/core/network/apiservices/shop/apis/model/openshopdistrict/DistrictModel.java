
package com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Deprecated
public class DistrictModel {

    @SerializedName(value = "province_id", alternate = {"city_id", "district_id"})
    @Expose
    private Integer id;
    @SerializedName(value = "cities", alternate = "districts")
    @Expose
    private List<DistrictModel> child = null;
    @SerializedName(value = "province_name", alternate = {"city_name", "district_name"})
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<DistrictModel> getChild() {
        return child;
    }

    public void setChild(List<DistrictModel> child) {
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
