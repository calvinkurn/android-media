package com.tokopedia.core.network.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/29/17.
 */

@Deprecated
public class ErrorHttpEntity {
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("status")
    @Expose
    int status;
    @SerializedName("title")
    @Expose
    String title;

    public ErrorHttpEntity() {
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
