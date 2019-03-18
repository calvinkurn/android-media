package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class ProductPreorder {

    @SerializedName("duration_text")
    @Expose
    private String durationText = "";
    @SerializedName("duration_day")
    @Expose
    private int durationDay;
    @SerializedName("duration_unit_code")
    @Expose
    private int durationUnitCode;
    @SerializedName("duration_unit_text")
    @Expose
    private String durationUnitText = "";
    @SerializedName("duration_value")
    @Expose
    private int durationValue;

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

    public int getDurationValue() {
        return durationValue;
    }
}
