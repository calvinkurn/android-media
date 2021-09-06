package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityPackage {

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "[package:{" +" "+
                "address=" + address +" "+
                "displayName=" + displayName
                + "}]";
    }

}
