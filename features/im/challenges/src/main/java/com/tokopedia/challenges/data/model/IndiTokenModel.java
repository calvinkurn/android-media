package com.tokopedia.challenges.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishal Gupta on 8/7/18.
 */
public class IndiTokenModel {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}