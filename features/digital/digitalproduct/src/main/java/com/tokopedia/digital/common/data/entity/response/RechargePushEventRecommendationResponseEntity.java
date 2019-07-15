package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by resakemal on 01/07/19.
 */
public class RechargePushEventRecommendationResponseEntity {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsError")
    @Expose
    private boolean isError;

    public String getMessage() {
        return message;
    }

    public boolean isError() { return isError; }
}
