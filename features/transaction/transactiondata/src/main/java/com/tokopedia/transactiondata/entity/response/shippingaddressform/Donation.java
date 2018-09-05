package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Irfan Khoirul on 13/07/18.
 */

public class Donation {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Nominal")
    @Expose
    private int nominal;
    @SerializedName("Description")
    @Expose
    private String description;

    public String getTitle() {
        return title;
    }

    public int getNominal() {
        return nominal;
    }

    public String getDescription() {
        return description;
    }
}
