package com.tokopedia.affiliate.common.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/10/18.
 */
public class CheckQuotaPojo {
    @SerializedName("formatted")
    @Expose
    private String formatted;

    @SerializedName("number")
    @Expose
    private int number;

    @SerializedName("format")
    @Expose
    private int format;

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }
}
