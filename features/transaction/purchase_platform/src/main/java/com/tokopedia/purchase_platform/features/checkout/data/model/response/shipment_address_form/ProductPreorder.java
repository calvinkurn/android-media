package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

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
    @SerializedName("duration_unit_code")
    @Expose
    private int durationUnitCode;
    @SerializedName("duration_unit_text")
    @Expose
    private String durationUnitText;
    @SerializedName("duration_value")
    @Expose
    private String durationValue;

    public String getDurationText() {
        return durationText;
    }

    public int getDurationDay() {
        return durationDay;
    }

    public int getDurationUnitCode() {
        return durationUnitCode;
    }

    public String getDurationUnitText() {
        return durationUnitText;
    }

    public String getDurationValue() {
        return durationValue;
    }
}
