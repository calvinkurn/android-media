
package com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("provinces_cities_districts")
    @Expose
    private List<DistrictModel> districtModels = null;

    public List<DistrictModel> getDistrictModels() {
        return districtModels;
    }

    public void setDistrictModels(List<DistrictModel> districtModels) {
        this.districtModels = districtModels;
    }

}
