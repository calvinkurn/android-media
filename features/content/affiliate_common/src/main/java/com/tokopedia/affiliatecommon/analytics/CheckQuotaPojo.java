package com.tokopedia.affiliatecommon.analytics;

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
    private String format;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
