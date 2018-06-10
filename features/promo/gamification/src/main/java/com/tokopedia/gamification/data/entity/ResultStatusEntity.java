package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class ResultStatusEntity {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("message")
    @Expose
    private List<String> message;

    @SerializedName("status")
    @Expose
    private String status;

    public String getCode() {
        return code;
    }

    public List<String> getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
