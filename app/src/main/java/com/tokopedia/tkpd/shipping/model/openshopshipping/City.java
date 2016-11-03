
package com.tokopedia.tkpd.shipping.model.openshopshipping;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("districts")
    @Expose
    private List<District> districts = new ArrayList<District>();
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("city_name")
    @Expose
    private String cityName;

    /**
     * 
     * @return
     *     The districts
     */
    public List<District> getDistricts() {
        return districts;
    }

    /**
     * 
     * @param districts
     *     The districts
     */
    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    /**
     * 
     * @return
     *     The cityId
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * 
     * @param cityId
     *     The city_id
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * 
     * @return
     *     The cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * 
     * @param cityName
     *     The city_name
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
