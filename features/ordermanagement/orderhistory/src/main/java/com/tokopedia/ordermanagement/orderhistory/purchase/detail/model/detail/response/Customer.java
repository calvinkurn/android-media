package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 1/15/18. Tokopedia
 */

public class Customer {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("picture")
    @Expose
    String picture = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
