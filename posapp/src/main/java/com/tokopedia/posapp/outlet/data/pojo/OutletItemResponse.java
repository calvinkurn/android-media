package com.tokopedia.posapp.outlet.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 3/28/18.
 */

public class OutletItemResponse {
    @SerializedName("")
    @Expose
    private String outletId;
    @SerializedName("")
    @Expose
    private String shopId;
    @SerializedName("")
    @Expose
    private String name;
    @SerializedName("")
    @Expose
    private String address;

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
