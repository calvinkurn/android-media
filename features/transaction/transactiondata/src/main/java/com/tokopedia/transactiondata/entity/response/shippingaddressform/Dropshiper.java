package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class Dropshiper {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("telp_no")
    @Expose
    private String telpNo;

    public Dropshiper() {
    }

    public String getName() {
        return name;
    }

    public String getTelpNo() {
        return telpNo;
    }
}
