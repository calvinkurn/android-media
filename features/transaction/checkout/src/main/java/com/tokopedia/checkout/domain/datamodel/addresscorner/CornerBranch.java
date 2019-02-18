
package com.tokopedia.checkout.domain.datamodel.addresscorner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CornerBranch {

    @SerializedName("corner_id")
    @Expose
    private Integer cornerId;
    @SerializedName("partner_id")
    @Expose
    private Integer partnerId;
    @SerializedName("corner_branch_name")
    @Expose
    private String cornerBranchName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("province_id")
    @Expose
    private Integer provinceId;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("district_id")
    @Expose
    private Integer districtId;
    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("province_name")
    @Expose
    private String provinceName;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("addr_desc")
    @Expose
    private String addrDesc;
    @SerializedName("geoloc")
    @Expose
    private String geoloc;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getCornerId() {
        return cornerId;
    }

    public void setCornerId(Integer cornerId) {
        this.cornerId = cornerId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getCornerBranchName() {
        return cornerBranchName;
    }

    public void setCornerBranchName(String cornerBranchName) {
        this.cornerBranchName = cornerBranchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddrDesc() {
        return addrDesc;
    }

    public void setAddrDesc(String addrDesc) {
        this.addrDesc = addrDesc;
    }

    public String getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(String geoloc) {
        this.geoloc = geoloc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
