package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityAddress {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassPojo [email = " + email + ", name = " + name + "]";
    }

}
