package com.tokopedia.core.addtocart.model.responseatcform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProvinceList {

    @SerializedName("province_name")
    @Expose
    private String provinceName;
    @SerializedName("province_id")
    @Expose
    private Integer provinceId;

    /**
     * @return The provinceName
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * @param provinceName The province_name
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * @return The provinceId
     */
    public Integer getProvinceId() {
        return provinceId;
    }

    /**
     * @param provinceId The province_id
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

}
