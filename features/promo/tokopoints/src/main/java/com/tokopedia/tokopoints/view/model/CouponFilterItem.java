package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CouponFilterItem {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("isSelected")
    private boolean isSelected;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "CouponFilterItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
