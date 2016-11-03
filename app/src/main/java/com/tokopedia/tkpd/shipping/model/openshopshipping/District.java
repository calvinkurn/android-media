
package com.tokopedia.tkpd.shipping.model.openshopshipping;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class District {

    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("district_id")
    @Expose
    private Integer districtId;
    @SerializedName("district_shipping_supported")
    @Expose
    private List<String> districtShippingSupported = new ArrayList<String>();

    /**
     * 
     * @return
     *     The districtName
     */
    public String getDistrictName() {
        return districtName;
    }

    /**
     * 
     * @param districtName
     *     The district_name
     */
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    /**
     * 
     * @return
     *     The districtId
     */
    public Integer getDistrictId() {
        return districtId;
    }

    /**
     * 
     * @param districtId
     *     The district_id
     */
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    /**
     * 
     * @return
     *     The districtShippingSupported
     */
    public List<String> getDistrictShippingSupported() {
        return districtShippingSupported;
    }

    /**
     * 
     * @param districtShippingSupported
     *     The district_shipping_supported
     */
    public void setDistrictShippingSupported(List<String> districtShippingSupported) {
        this.districtShippingSupported = districtShippingSupported;
    }

}
