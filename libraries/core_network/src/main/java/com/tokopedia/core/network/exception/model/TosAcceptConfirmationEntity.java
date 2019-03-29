package com.tokopedia.core.network.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/29/17.
 */

@Deprecated
public class TosAcceptConfirmationEntity {
    @SerializedName("href")
    @Expose
    String href;
    @SerializedName("tos_confirmation_id")
    @Expose
    String tosId;

    public TosAcceptConfirmationEntity() {
    }

    public String getHref() {
        return href;
    }

    public String getTosId() {
        return tosId;
    }
}
