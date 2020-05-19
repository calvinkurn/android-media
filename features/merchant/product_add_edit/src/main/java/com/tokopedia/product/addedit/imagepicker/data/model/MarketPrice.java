
package com.tokopedia.product.addedit.imagepicker.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketPrice {

    @SerializedName("min")
    @Expose
    private int min;
    @SerializedName("max")
    @Expose
    private int max;
    @SerializedName("min_fmt")
    @Expose
    private String minFmt;
    @SerializedName("max_fmt")
    @Expose
    private String maxFmt;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("name")
    @Expose
    private String name;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getMinFmt() {
        return minFmt;
    }

    public void setMinFmt(String minFmt) {
        this.minFmt = minFmt;
    }

    public String getMaxFmt() {
        return maxFmt;
    }

    public void setMaxFmt(String maxFmt) {
        this.maxFmt = maxFmt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
