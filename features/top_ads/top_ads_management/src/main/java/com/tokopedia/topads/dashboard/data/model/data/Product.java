package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nathaniel on 12/21/2016.
 */

public class Product {
    @SerializedName("product_id")
    @Expose
    private int id;
    @SerializedName("product_name")
    @Expose
    private String name;
    @SerializedName("product_image")
    @Expose
    private String imageUrl;
    @SerializedName("product_is_promoted")
    @Expose
    private boolean isPromoted;
    @SerializedName("ad_id")
    @Expose
    private int adId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("department_id")
    @Expose
    private int departmentId;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}