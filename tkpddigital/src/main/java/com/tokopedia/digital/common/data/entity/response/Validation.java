package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class Validation {

    @SerializedName("regex")
    @Expose
    private String regex;
    @SerializedName("error")
    @Expose
    private String error;

    public String getRegex() {
        return regex;
    }

    public String getError() {
        return error;
    }
}
