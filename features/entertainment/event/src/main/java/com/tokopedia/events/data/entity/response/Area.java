package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Area {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("areaCode")
    @Expose
    private String areaCode;
    @SerializedName("areaNo")
    @Expose
    private int areaNo;
    @SerializedName("isSelected")
    @Expose
    private String isSelected;
    @SerializedName("seatReservedCount")
    @Expose
    private int seatReservedCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(int areaNo) {
        this.areaNo = areaNo;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public int getSeatReservedCount() {
        return seatReservedCount;
    }

    public void setSeatReservedCount(int seatReservedCount) {
        this.seatReservedCount = seatReservedCount;
    }

}
