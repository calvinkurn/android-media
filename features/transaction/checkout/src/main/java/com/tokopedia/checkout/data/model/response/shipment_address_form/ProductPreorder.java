package com.tokopedia.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/10/18.
 */

public class ProductPreorder {

    @SerializedName("duration_text")
    @Expose
    private String durationText;
    @SerializedName("duration_day")
    @Expose
    private int durationDay;

    public String getDurationText() {
        return durationText;
    }

    public int getDurationDay() {
        return durationDay;
    }

}
