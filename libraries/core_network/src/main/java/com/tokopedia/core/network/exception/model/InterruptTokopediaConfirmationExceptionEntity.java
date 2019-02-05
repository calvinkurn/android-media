package com.tokopedia.core.network.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 7/14/17.
 */

@Deprecated
public class InterruptTokopediaConfirmationExceptionEntity {
    @SerializedName("href")
    @Expose
    String href;
    @SerializedName("tos_tokopedia_id")
    @Expose
    String tosId;

    public InterruptTokopediaConfirmationExceptionEntity() {
    }

    public String getHref() {
        return href;
    }

    public String getTosId() {
        return tosId;
    }
}
