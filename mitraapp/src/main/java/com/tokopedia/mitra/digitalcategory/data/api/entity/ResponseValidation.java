package com.tokopedia.mitra.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 31/08/18.
 */
class ResponseValidation {

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
