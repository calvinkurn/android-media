package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 12/04/19.
 */
public class DebitInstantData {

    @SerializedName("redirectURL")
    @Expose
    private String redirectUrl = "";

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
